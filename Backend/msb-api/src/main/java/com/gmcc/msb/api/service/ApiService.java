package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.ApiAudit;
import com.gmcc.msb.api.entity.Serv;
import com.gmcc.msb.api.repository.ApiGroupItemRepository;
import com.gmcc.msb.api.repository.ApiRepository;
import com.gmcc.msb.api.vo.ApiInfo;
import com.gmcc.msb.api.vo.request.AuditRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author Yuan Chunhai
 */
@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private ApiGroupItemRepository apiGroupItemRepository;

    @Autowired
    private ServService servService;

    @Autowired
    private AppOrderApiService appOrderApiService;

    @Autowired
    private ApiGroupItemService apiGroupItemService;

    @Autowired
    private AppService appService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ApiAuditService apiAuditService;

    public API add(API api) {
        this.check(api);
        api.setStatus(API.STATUS_NEW);

        Serv serv = servService.findOne(api.getServiceId());
        if (serv == null) {
            throw new MsbException("0001-10110");
        }

        return this.apiRepository.save(api);
    }

    private void check(API api) {
        if (!checkMethod(api.getMethod())) {
            throw new MsbException("0001-10111");
        }
        if (!checkPath(api.getPath())) {
            throw new MsbException("0001-10112");
        }
        String method = api.getMethod().toUpperCase();

        Iterable<API> find = apiRepository.findByServiceIdAndPathAndMethod(api.getServiceId(), api.getPath(), method);
        if (find != null) {
            Iterator<API> it = find.iterator();
            while (it.hasNext()) {
                API next = it.next();
                if (!next.getId().equals(api.getId())) {
                    throw new MsbException("0001-10113");
                }
            }
        }

        api.setMethod(method);
    }

    private boolean checkMethod(String method) {
        if (method == null) {
            return false;
        }
        String regex = "(GET|POST|PUT|DELETE|HEADER|OPTIONS)";
        return method.toUpperCase().matches(regex);
    }

    private boolean checkPath(String path) {
        if (path == null) {
            return false;
        }
        final String regex = "(/[a-zA-Z0-9_\\-\\{\\}]+){1,}/$";
        return path.matches(regex);
    }

    public void syncRedis(API api) {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(api, apiInfo);
        redisService.addApiInfo(apiInfo);
    }

    public API update(Integer id, API api) {
        checkApiId(id);
        API find = this.apiRepository.findOne(id);
        if (find == null) {
            throw new MsbException("0001-10114");
        }
        if (find.getStatus() != API.STATUS_NEW
                    && find.getStatus() != API.STATUS_APPLY_ONLINE_FAIL
                    && find.getStatus() != API.STATUS_OFFLINE) {
            throw new MsbException("0001-10115");
        }

        if (!StringUtils.isEmpty(api.getApiName())) {
            find.setApiName(api.getApiName());
        }
        if (!StringUtils.isEmpty(api.getMethod())) {
            find.setMethod(api.getMethod());
        }
        if (!StringUtils.isEmpty(api.getPath())) {
            find.setPath(api.getPath());
        }
        if (!StringUtils.isEmpty(api.getServiceId())) {
            find.setServiceId(api.getServiceId());
        }
        if (!StringUtils.isEmpty(api.isAnnoymousAccess())) {
            find.setAnnoymousAccess(api.isAnnoymousAccess());
        }
        this.check(find);

        return this.apiRepository.save(find);
    }

    private void checkApiId(Integer id) {
        Assert.notNull(id, "0001-00016");
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(Integer apiId) {
        checkApiId(apiId);
        API find = this.apiRepository.findOne(apiId);
        if (find == null) {
            throw new MsbException("0001-10116");
        }
        if (find.getStatus() != null
                    && find.getStatus() != API.STATUS_NEW
                    && find.getStatus() != API.STATUS_OFFLINE
                    && find.getStatus() != API.STATUS_APPLY_ONLINE_FAIL) {
            throw new MsbException("0001-10117");
        }

       if(apiRepository.hasCircuit(apiId)>0){
           throw new MsbException("0001-10140");
       }
        if(apiRepository.hasFluid(apiId)>0){
            throw new MsbException("0001-10141");
        }
        apiGroupItemRepository.deleteByApiId(apiId);
        this.apiRepository.delete(apiId);
    }

    public API findOne(Integer id) {
        return this.apiRepository.findOne(id);
    }

    public Page<API> find(String serviceId, String serviceName, String apiName, Integer status, Pageable pageable) {
        List<Long> orgList = UserContextHolder.getContext().getDataOrgList();
        if(orgList.size()==0){

            return new PageImpl<API>(new ArrayList<>(),pageable,0);
        }

        if (StringUtils.isEmpty(serviceId)) {
            serviceId = null;
        } else {
            serviceId = "%" + serviceId + "%";
        }
        if (StringUtils.isEmpty(apiName)) {
            apiName = null;
        } else {
            apiName = "%" + apiName + "%";
        }
        if (StringUtils.isEmpty(serviceName)) {
            serviceName = null;
        } else {
            serviceName = "%" + serviceName + "%";
        }
        return this.apiRepository.findAllByConditions(apiName, serviceId, serviceName, status, orgList,pageable);

    }

    public Page<API> findAll(String serviceId, String serviceName, String apiName, Integer status, Pageable pageable) {

        if (StringUtils.isEmpty(serviceId)) {
            serviceId = null;
        } else {
            serviceId = "%" + serviceId + "%";
        }
        if (StringUtils.isEmpty(apiName)) {
            apiName = null;
        } else {
            apiName = "%" + apiName + "%";
        }
        if (StringUtils.isEmpty(serviceName)) {
            serviceName = null;
        } else {
            serviceName = "%" + serviceName + "%";
        }
        return this.apiRepository.findAll(apiName, serviceId, serviceName, status,pageable);

    }

    public List<API> findAllServiceApis(String serviceId) {
        return this.apiRepository.findAllByServiceIdEquals(serviceId);
    }


    /**
     * 查找所有已上线的api
     */
    public Iterable<API> findAllOnlineApisByServiceId(String serviceId) {
        return this.apiRepository.findAllByServiceIdEqualsAndStatusEquals(serviceId, API.STATUS_ONLINE);
    }

    /**
     * 上线、下线申请
     *
     * @param type 0上线申请，1下线申请
     */
    @Transactional
    public API apply(Integer apiId, Integer type) {
        checkApiId(apiId);
        API api = this.findOne(apiId);
        checkApiNull(api);
        if (type == 0) {
            //申请上线
            if (api.getStatus() == API.STATUS_NEW || api.getStatus() == API.STATUS_APPLY_ONLINE_FAIL
                        || api.getStatus() == API.STATUS_OFFLINE) {
                //申请上线
                api.setStatus(API.STATUS_APPLY_ONLINE);
            } else {
                throw new MsbException("0001-10118");
            }
        } else if (type == 1) {
            if (api.getStatus() == API.STATUS_ONLINE || api.getStatus() == API.STATUS_APPLY_OFFLINE_FAIL) {
                apiCanOffline(api);
                //申请下线
                api.setStatus(API.STATUS_APPLY_OFFLINE);
            } else {
                throw new MsbException("0001-10119");
            }
        } else {
            throw new MsbException("0001-10120");
        }
        API saved = this.apiRepository.save(api);

        this.apiAuditService.create(saved, type);

        //申请下线后，同步reids，禁止访问api
        if (type == 1) {
            this.syncRedis(saved);
        }
        return saved;
    }

    /**
     * api 是否能下线检查
     */
    private void apiCanOffline(API api) {
        // 服务未被订阅才能申请下线
        boolean ordered = appOrderApiService.isApiOrderedByApp(api.getId());
        if (ordered) {
            throw new MsbException("0001-10121");
        }
        //如果API存在于某个分组中，不能下线
//        boolean inGroup = apiGroupItemService.isApiInGroup(api.getId());
//        if (inGroup) {
//            throw new MsbException("0001-10122");
//        }
    }

    private void checkApiNull(API api) {
        if (api == null) {
            throw new MsbException("0001-10123");
        }
    }

    /**
     * 上线、下线审核
     */
    @Transactional
    public API audit(Integer apiAuditId, AuditRequest vo) {
        Assert.notNull(apiAuditId, "0001-10126");
        ApiAudit apiAudit = this.apiAuditService.findOne(apiAuditId);
        Assert.notNull(apiAudit, "0001-10127");
        API api = this.findOne(apiAudit.getApiId());
        checkApiNull(api);
        if (vo.getType() == 0) {
            //上线申请
            audit(vo, api, API.STATUS_APPLY_ONLINE, "0001-10124", API.STATUS_ONLINE, API.STATUS_APPLY_ONLINE_FAIL);
        } else if (vo.getType() == 1) {
            //申请下线
            audit(vo, api, API.STATUS_APPLY_OFFLINE, "0001-10125", API.STATUS_OFFLINE, API.STATUS_APPLY_OFFLINE_FAIL);
        }
        API saved = this.apiRepository.save(api);

        apiAudit.setAuditResult(vo.getResult());
        apiAudit.setAuditDate(new Date());
        apiAudit.setUpdateTime(apiAudit.getAuditDate());
        apiAudit.setAuditBy(UserContextHolder.getContext().getUserId());

        this.apiAuditService.save(apiAudit);

        //审批通过后（上线），同步redis
        if (vo.getType() == 0 && vo.getResult() == 0) {
            this.syncRedis(saved);
        }
        return saved;
    }

    /**
     * @param vo
     * @param api
     * @param checkStatus     审核之前的状态，如果不是该状态，则抛出异常
     * @param errMsg          审核之前的状态不正确的错误信息
     * @param auditPassStatus 审核通过之后的状态
     * @param auditFailStatus 审核不通过后的状态
     */
    private void audit(AuditRequest vo, API api, int checkStatus, String errMsg,
                       int auditPassStatus, int auditFailStatus) {
        if (api.getStatus() != checkStatus) {
            throw new MsbException(errMsg);
        } else {
            if (vo.getResult() == 0) {
                //审核通过
                api.setStatus(auditPassStatus);
            } else {
                //审核不通过
                api.setStatus(auditFailStatus);
            }
        }
    }

    public API setServiceName(API api) {
        if (api == null) {
            return api;
        }
        Serv service = servService.findOne(api.getServiceId());
        if (service != null) {
            api.setServiceName(service.getServiceName());
        }
        return api;
    }

    public Page<Map<String, Object>> appsThatOrderApiId(Integer appId, Pageable pageable) {
        return this.appService.appsOrderApiId(appId, pageable);
    }


    public List<API> findAllApisInGroup(Integer groupId) {
        Assert.notNull(groupId, "0001-10128");
        return this.apiRepository.findAllInGroup(groupId);
    }

    public Page<ApiAudit> getAuditList(Pageable pageable) {
        return this.apiAuditService.findAll(pageable);
    }


    public List<API> findByids(List<Integer> ids) {
        return this.apiRepository.findAllByIdIn(ids);
    }


    /**
     * 获取所有上线api数量
     *
     * @return
     */
    public int getAllOnlineApiCount() {
        return this.apiRepository.countAllByStatusEquals(API.STATUS_ONLINE);
    }

}
