package com.gmcc.msb.msbbreak.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbbreak.common.BreakerParam;
import com.gmcc.msb.msbbreak.entity.BreakerStrategy;
import com.gmcc.msb.msbbreak.entity.BreakerStrategyApi;
import com.gmcc.msb.msbbreak.entity.ServiceApi;
import com.gmcc.msb.msbbreak.entity.ServiceBreaker;
import com.gmcc.msb.msbbreak.repository.*;
import com.gmcc.msb.msbbreak.vo.resp.ApiResp;
import com.gmcc.msb.msbbreak.vo.resp.ApiVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.gmcc.msb.msbbreak.common.CommonConstant.*;

@Service
public class BreakerService {
    @Autowired
    private BreakerStrategyRepository breakerStrategyRepository;
    @Autowired
    private ServiceApiRepository serviceApiRepository;
    @Autowired
    private ServiceBreakerRepository serviceBreakerRepository;
    @Autowired
    private BreakerStrategyApiRepository breakerStrategyApiRepository;

    /**
     * 查找API列表
     *
     * @param serviceApi
     * @return
     */
    public List<ServiceApi> findAllServiceApi(ServiceApi serviceApi) {
        return serviceApiRepository.findAll(ServiceApiSpec.queryApiListSpec(serviceApi), new Sort(Sort.Direction.DESC, "id"));
    }

    /**
     * 查找非默认策略列表
     *
     * @param breakerStrategy
     * @return
     */
    public List<BreakerStrategy> findAllBreakerStrategy(BreakerStrategy breakerStrategy) {
        if (UserContextHolder.getContext().getDataOrgList().isEmpty()) {
            return Lists.newArrayList();
        }
        return breakerStrategyRepository.findAll(BreakerStrategySpec.queryBreakerListSpec(breakerStrategy), new Sort(Sort.Direction.DESC, "createTime"));
    }

    /**
     * 添加，修改熔断策略
     *
     * @param breakerStrategy
     */
    @Transactional
    public Map<String, Object> manageBreakStrategy(BreakerStrategy breakerStrategy) throws MsbException {
        BreakerStrategy oldBreaker = null;
        if (breakerStrategy.getIsDefault()) {
            if (breakerStrategyRepository.countByStrategyNameAndIsDefaultNot(
                    breakerStrategy.getStrategyName()) > 0) {
                throw new MsbException(ERROR_STRATEGY_NAME_DUPLICATE, "熔断策略名称重复");
            }
            oldBreaker = breakerStrategyRepository.findByIsDefault(true);
        } else if (breakerStrategy.getId() != null) {
            oldBreaker = breakerStrategyRepository.findOne(breakerStrategy.getId());
            if (oldBreaker == null) {
                throw new MsbException(ERROR_STRATEGY_NOT_EXIST, "熔断策略不存在");
            }
            if (oldBreaker.getIsDefault()) {
                throw new MsbException(ERROR_IS_DEFAULT, "此操作无权修改默认策略");
            }

            if (breakerStrategyRepository.countByStrategyNameAndIdNot(
                    breakerStrategy.getStrategyName(), breakerStrategy.getId()) > 0) {
                throw new MsbException(ERROR_STRATEGY_NAME_DUPLICATE, "熔断策略名称重复");
            }
        } else {
            if (breakerStrategyRepository.countByStrategyName(
                    breakerStrategy.getStrategyName()) > 0) {
                throw new MsbException(ERROR_STRATEGY_NAME_DUPLICATE, "熔断策略名称重复");
            }
        }
        if (oldBreaker != null) {
            oldBreaker.setUpdateTime(new Date());
            oldBreaker.setEnableBreaker(breakerStrategy.getEnableBreaker());
            oldBreaker.setFailRate(breakerStrategy.getFailRate());
            oldBreaker.setRequestVolume(breakerStrategy.getRequestVolume());
            oldBreaker.setSleep(breakerStrategy.getSleep());
            oldBreaker.setTimeout(breakerStrategy.getTimeout());
            oldBreaker.setStrategyName(breakerStrategy.getStrategyName());
            breakerStrategyRepository.save(oldBreaker);
            breakerStrategy.setId(oldBreaker.getId());
        } else {
            breakerStrategy.setCreateTime(new Date());
            BreakerStrategy newStrategy = breakerStrategyRepository.save(breakerStrategy);
            breakerStrategy.setId(newStrategy.getId());
        }

        if (breakerStrategy.getIsDefault()) {
            /*发布默认策略到熔断策略表，zuul会轮询该表的数据,默认策略对所有微服务生效*/
            return publishBreakStrategy(breakerStrategy, null);
        } else if (oldBreaker != null) {
            /*将修改后的配置刷新到配置表*/
            return refreshBreakerStrategy(oldBreaker);
        }

        return null;
    }

    /**
     * 查找一个熔断策略
     *
     * @param id
     * @return
     */
    public BreakerStrategy findBreakerStrategy(Integer id) {
        return breakerStrategyRepository.findBreakerStrategyByIdNoDefault(id);
    }

    /**
     * 查找默认策略
     *
     * @return
     */
    public BreakerStrategy findBreakerStrategyDefault() {
        return breakerStrategyRepository.findByIsDefault(true);
    }

    /**
     * 查找一个service api
     *
     * @param id
     * @return
     */
    public ServiceApi findServiceApi(Integer id) {
        return serviceApiRepository.findOne(id);
    }

    /**
     * 查找一个API绑定熔断策略
     *
     * @param id
     * @return
     */
    public BreakerStrategy findBreakerStrategyByApiId(Integer id) {
        return breakerStrategyRepository.findBreakerStrategyByApiId(id);
    }

    /**
     * 打开一个熔断策略
     *
     * @param id
     */
    @Transactional
    public void enableBreaker(Integer id) {
        breakerStrategyRepository.updateBreakerStatus(id, true);
        serviceBreakerRepository.updateStrategy("true", id, BreakerParam.ENABLE_BREAKER_TYPE);
    }

    /**
     * 关闭一个熔断策略
     *
     * @param id
     */
    @Transactional
    public void disableBreaker(Integer id) {
        breakerStrategyRepository.updateBreakerStatus(id, false);
        serviceBreakerRepository.updateStrategy("false", id, BreakerParam.ENABLE_BREAKER_TYPE);
    }

    /**
     * 删除一个熔断策略
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBreaker(Integer id) {
        breakerStrategyRepository.delete(id);
        breakerStrategyApiRepository.deleteByStrategyId(id);
        serviceBreakerRepository.deleteByStrategyId(id);
    }

    /**
     * 批量API绑定熔断策略
     *
     * @param strategy
     * @param apiIdList
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindBreakerStrategyApiBatch(BreakerStrategy strategy, List<Integer> apiIdList) {
        if (strategy.getIsDefault()) {
            throw new MsbException(ERROR_DEFAULT_CAN_NOT_BIND, "不能绑定默认策略");
        }
        List<ServiceApi> apiList = serviceApiRepository.findApiByIdList(apiIdList);

        if(apiList!=null&&apiList.size()>0){
            //一个API只能有一个熔断策略，先删除原有的熔断策略
            breakerStrategyApiRepository.deleteByApiIdList(apiIdList);
            serviceBreakerRepository.deleteByApiIdList(apiIdList);

            //批量构建绑定关系
            List<BreakerStrategyApi> bsa = Lists.newArrayList();
            List<ServiceBreaker> sblist = Lists.newArrayList();
            for(ServiceApi serviceApi:apiList){
                BreakerStrategyApi breakerStrategyApI = new BreakerStrategyApi();
                breakerStrategyApI.setApiId(serviceApi.getId());
                breakerStrategyApI.setStrategyId(strategy.getId());
                breakerStrategyApI.setCreateTime(new Date());
                bsa.add(breakerStrategyApI);

                String commandKey = serviceApi.getServiceId() + "#" + serviceApi.getId();
                List<ServiceBreaker> breakers = buildServiceBreaker(commandKey,strategy,serviceApi,null,null);
                sblist.addAll(breakers);
            }
            breakerStrategyApiRepository.save(bsa);
            serviceBreakerRepository.save(sblist);
        }

    }

    /**
     * 批量删除API绑定的熔断策略
     *
     * @param apiIdList
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBreakerStrategyApiBatch(List<Integer> apiIdList) {
        breakerStrategyApiRepository.deleteByApiIdList(apiIdList);
        serviceBreakerRepository.deleteByApiIdList(apiIdList);
    }



    /**
     * API绑定熔断策略
     *
     * @param strategy
     * @param serviceApi
     * @throws MsbException
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createBreakerStrategyApi(BreakerStrategy strategy, ServiceApi serviceApi) throws MsbException {
        if (strategy == null || serviceApi == null) {
            throw new MsbException(ERROR_API_NOT_EXIST, "api不存在");
        }
        if (strategy.getIsDefault()) {
            throw new MsbException(ERROR_DEFAULT_CAN_NOT_BIND, "不能绑定默认策略");
        }

        //一个API只能有一个熔断策略，先删除原有的熔断策略
        breakerStrategyApiRepository.deleteByApiId(serviceApi.getId());

        BreakerStrategyApi breakerStrategyApI = new BreakerStrategyApi();
        breakerStrategyApI.setApiId(serviceApi.getId());
        breakerStrategyApI.setStrategyId(strategy.getId());
        breakerStrategyApI.setCreateTime(new Date());
        breakerStrategyApiRepository.save(breakerStrategyApI);

        return publishBreakStrategy(strategy, serviceApi);

    }

    /**
     * 删除API绑定的熔断策略
     *
     * @param apiId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBreakerStrategyApi(Integer apiId) {
        breakerStrategyApiRepository.deleteByApiId(apiId);
        serviceBreakerRepository.deleteByApiId(apiId);
    }

    private Map<String, Object> refreshBreakerStrategy(BreakerStrategy breakerStrategy) {

        Map<String, String> updateMap = new HashMap<>();
        Set<String> deleteSet = new HashSet<>();
        if (breakerStrategy.getEnableBreaker() != null) {
            updateMap.put(BreakerParam.ENABLE_BREAKER_TYPE, breakerStrategy.getEnableBreaker().toString());
        } else {
            deleteSet.add(BreakerParam.ENABLE_BREAKER_TYPE);
        }
        if (breakerStrategy.getFailRate() != null) {
            updateMap.put(BreakerParam.FAIL_RATE_TYPE, breakerStrategy.getFailRate().toString());
        } else {
            deleteSet.add(BreakerParam.FAIL_RATE_TYPE);
        }

        if (breakerStrategy.getRequestVolume() != null) {
            updateMap.put(BreakerParam.REQUEST_VOLUME_TYPE, breakerStrategy.getRequestVolume().toString());
        } else {
            deleteSet.add(BreakerParam.REQUEST_VOLUME_TYPE);
        }
        if (breakerStrategy.getSleep() != null) {
            updateMap.put(BreakerParam.SLEEP_TYPE, breakerStrategy.getSleep().toString());
        } else {
            deleteSet.add(BreakerParam.SLEEP_TYPE);
        }
        if (breakerStrategy.getTimeout() != null) {
            updateMap.put(BreakerParam.TIMEOUT_TYPE, breakerStrategy.getTimeout().toString());
        } else {
            deleteSet.add(BreakerParam.TIMEOUT_TYPE);
        }


        /*更新配置*/
        for (Map.Entry<String, String> entry : updateMap.entrySet()) {
            serviceBreakerRepository.updateStrategy(entry.getValue(), breakerStrategy.getId(), entry.getKey());
        }
        /*删除配置*/
        for (String key : deleteSet) {
            serviceBreakerRepository.deleteByStrategyIdAndType(breakerStrategy.getId(), key);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("update", updateMap);
        result.put("delete", deleteSet);

        return result;

    }

    private Map<String, Object> publishBreakStrategy(BreakerStrategy breakerStrategy, ServiceApi api) {
        Map<String, String> proKeyMap = new HashMap<>();
        Map<String, String> typeMap = new HashMap<>();

        String commandKey = "";
        if (breakerStrategy.getIsDefault()) {
            commandKey = BreakerParam.DEFAULT_KEY;
        } else {
            commandKey = api.getServiceId() + "#" + api.getId();
        }

        List<ServiceBreaker> breakers = buildServiceBreaker(commandKey,breakerStrategy,api,proKeyMap,typeMap);

        Set<String> keySet = proKeyMap.keySet();
        if (!keySet.isEmpty()) {
            if (breakerStrategy.getIsDefault()) {
                /*删除旧的默认配置*/
                serviceBreakerRepository.deleteByStrategyId(breakerStrategy.getId());
            } else {
                /*删除此API的旧的配置*/
                serviceBreakerRepository.deleteByApiId(api.getId());
            }
        }


        /*插入新配置*/
        if (!breakers.isEmpty()) {
            serviceBreakerRepository.save(breakers);
        }

        Map<String, Object> result = new HashMap<>();

        result.put("key", proKeyMap);
        result.put("type", typeMap);

        return result;

    }

    private List<ServiceBreaker> buildServiceBreaker(
            String commandKey, BreakerStrategy breakerStrategy, ServiceApi api,
            Map<String, String> proKeyMap, Map<String, String> typeMap) {
        List<ServiceBreaker> list = Lists.newArrayList();
        if (proKeyMap == null) {
            proKeyMap = new HashMap<>();
        }
        if (typeMap == null) {
            typeMap = new HashMap<>();
        }

        String prefix = BreakerParam.PARAM_PRE + "." + commandKey + ".";
        if (breakerStrategy.getEnableBreaker() != null) {
            proKeyMap.put(prefix + BreakerParam.ENABLE_BREAKER_SUF, breakerStrategy.getEnableBreaker().toString());
            typeMap.put(prefix + BreakerParam.ENABLE_BREAKER_SUF, BreakerParam.ENABLE_BREAKER_TYPE);
        }
        if (breakerStrategy.getFailRate() != null) {
            proKeyMap.put(prefix + BreakerParam.FAIL_RATE_SUF, breakerStrategy.getFailRate().toString());
            typeMap.put(prefix + BreakerParam.FAIL_RATE_SUF, BreakerParam.FAIL_RATE_TYPE);
        }

        if (breakerStrategy.getRequestVolume() != null) {
            proKeyMap.put(prefix + BreakerParam.REQUEST_VOLUME_SUF, breakerStrategy.getRequestVolume().toString());
            typeMap.put(prefix + BreakerParam.REQUEST_VOLUME_SUF, BreakerParam.REQUEST_VOLUME_TYPE);
        }
        if (breakerStrategy.getSleep() != null) {
            proKeyMap.put(prefix + BreakerParam.SLEEP_SUF, breakerStrategy.getSleep().toString());
            typeMap.put(prefix + BreakerParam.SLEEP_SUF, BreakerParam.SLEEP_TYPE);
        }
        if (breakerStrategy.getTimeout() != null) {
            proKeyMap.put(prefix + BreakerParam.TIMEOUT_SUF, breakerStrategy.getTimeout().toString());
            typeMap.put(prefix + BreakerParam.TIMEOUT_SUF, BreakerParam.TIMEOUT_TYPE);
        }

        Set<String> keySet = proKeyMap.keySet();
        Date createTime = new Date();

        /*插入新配置*/
        for (String key : keySet) {
            ServiceBreaker sb = new ServiceBreaker();
            sb.setPropertyKey(key);
            sb.setPropertyValue(proKeyMap.get(key));
            sb.setCreateTime(createTime);
            sb.setApiId(api != null ? api.getId() : null);
            sb.setStrategyId(breakerStrategy.getId());
            sb.setType(typeMap.get(key));
            list.add(sb);
        }

        return list;
    }


    /**
     * 查找一个策略下的API信息
     * 返回对象ApiResp里有三个列表对象：
     * apiListInStrategy：已绑定本策略的API列表
     * apiListOtherStrategy：已绑定其他策略的API列表
     * apiListNoStrategy：未绑定熔断策略的API列表
     *
     * @param strategyId
     * @return
     */
    public ApiResp findApiList(Integer strategyId) {
        ApiResp result = new ApiResp();
        List<Long> orgIdList = UserContextHolder.getContext().getDataOrgList();
        if (orgIdList.isEmpty()) {
            return result;
        }
        List<ApiVo> apiVoList = breakerStrategyApiRepository.findApiVoInOrgs(orgIdList);
        List<ApiVo> allApiList = serviceApiRepository.findApiVoInOrgs(orgIdList);
        if (apiVoList != null) {
            for (ApiVo apiVo : apiVoList) {
                if (apiVo.getStrategyId().equals(strategyId)) {
                    result.getApiListInStrategy().add(apiVo);
                } else {
                    result.getApiListOtherStrategy().add(apiVo);
                }

            }

            for (ApiVo apiVo : allApiList) {
                if (!apiVoList.contains(apiVo)) {
                    result.getApiListNoStrategy().add(apiVo);
                }
            }
        } else {
            result.setApiListNoStrategy(allApiList);
        }


        return result;


    }


}
