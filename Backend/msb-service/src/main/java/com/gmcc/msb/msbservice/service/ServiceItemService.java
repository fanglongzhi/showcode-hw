package com.gmcc.msb.msbservice.service;

import com.gmcc.msb.common.entity.*;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.common.service.client.MsbApiClient;
import com.gmcc.msb.common.service.client.MsbConfigClient;
import com.gmcc.msb.common.service.client.MsbRouteClient;
import com.gmcc.msb.common.vo.Constant;
import com.gmcc.msb.common.vo.Response;
import com.gmcc.msb.msbservice.common.resp.CommonConstant;
import com.gmcc.msb.msbservice.entity.ErrorCode;
import com.gmcc.msb.msbservice.entity.ServiceItem;
import com.gmcc.msb.msbservice.repository.ServiceItemRepository;
import com.gmcc.msb.msbservice.util.KeyBuilder;
import com.gmcc.msb.msbservice.vo.ServiceItemVo;
import com.gmcc.msb.msbservice.vo.ServiceStatusResp;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author zhi fanglong
 */
@Service
@Lazy
public class ServiceItemService {

    private static final Logger log = LoggerFactory.getLogger(ServiceItemService.class);

    private int startServiceCode=1000;

    private DiscoveryClient discoveryClient;

    private ServiceItemRepository serviceItemRepository;

    @Autowired
    @Lazy
    private MsbApiClient msbApiClient;

    private MsbConfigClient msbConfigClient;

    @Autowired
    @Lazy
    private MsbRouteClient msbRouteClient;

    @Autowired
    private ErrorCodeService errorCodeService;

    @Autowired
    @Lazy
    public ServiceItemService(DiscoveryClient discoveryClient, ServiceItemRepository serviceItemRepository,MsbConfigClient msbConfigClient) {
        this.discoveryClient = discoveryClient;
        this.serviceItemRepository = serviceItemRepository;
        this.msbConfigClient = msbConfigClient;
    }

    public List<ServiceItem> findServiceItem() {
        List<Long> orgId = UserContextHolder.getContext().getDataOrgList();
        if(orgId==null||orgId.size()==0){
            return Lists.newArrayList();
        }
        return serviceItemRepository.findServiceItemInOrgs(orgId,new Sort(Sort.Direction.DESC, "id"));

    }

    public List<ServiceStatusResp> findServiceStatus(Integer id) {
        ServiceItem serviceItem = serviceItemRepository.findOne(id);
        if (serviceItem == null) {
            throw new MsbException(CommonConstant.ERROR_CODE, CommonConstant.ERROR_MSG);
        }
        List<ServiceInstance> list = discoveryClient.getInstances(serviceItem.getServiceId());
        List<ServiceStatusResp> statusList = Lists.newArrayList();
        for (ServiceInstance instance : list) {
            ServiceStatusResp resp = new ServiceStatusResp();
            resp.setServiceId(instance.getServiceId());
            resp.setIp(((EurekaDiscoveryClient.EurekaServiceInstance) instance).getInstanceInfo().getIPAddr());
            resp.setPort(instance.getPort());
            resp.setStatus(((EurekaDiscoveryClient.EurekaServiceInstance) instance).getInstanceInfo().getStatus().name());

            statusList.add(resp);
        }

        return statusList;
    }

    public List<String> syncServiceList() {
        List<String> serviceList = discoveryClient.getServices();
        List<ServiceItem> serviceItemList = Lists.newArrayList();
        if (serviceList != null) {
            for (String serviceId : serviceList) {
                if (serviceItemRepository.countByServiceId(serviceId.toLowerCase(), serviceId.toUpperCase()) == 0) {
                    ServiceItem item = new ServiceItem();
                    item.setCreateTime(new Date());
                    item.setUpdateTime(new Date());
                    item.setServiceId(serviceId.toLowerCase());
                    item.setServiceName(serviceId.toLowerCase());
                    item.setServiceSecret(KeyBuilder.getKey());
                    item.setUpdateBy("SYNC");
                    serviceItemList.add(item);
                }
            }

            if (!serviceItemList.isEmpty()) {
                serviceItemRepository.save(serviceItemList);
            }
        }

        return serviceList;
    }

    @Transactional
    public ServiceItem createServiceItem(ServiceItemVo param) {
        ServiceItem item = new ServiceItem();
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setServiceId(param.getServiceId().toLowerCase());
        item.setServiceName(param.getServiceName());
        item.setServiceSecret(KeyBuilder.getKey());
        int serviceCodeInt = startServiceCode;
        ServiceItem biggestServiceItem = serviceItemRepository.findFirstByOrderByServiceCodeDesc();
        if(biggestServiceItem!=null){
            String biggestServiceCode = biggestServiceItem.getServiceCode();
            if(biggestServiceCode!=null&&StringUtils.isNotEmpty(biggestServiceCode)){
                serviceCodeInt = Integer.valueOf(biggestServiceCode);
            }
        }

        if(serviceCodeInt<startServiceCode){
            serviceCodeInt=startServiceCode;
        }

        item.setServiceCode(String.valueOf(serviceCodeInt+1));
        ServiceItem newItem = serviceItemRepository.save(item);

        ServiceSecretInfo serviceSecretInfo = new ServiceSecretInfo();
        serviceSecretInfo.setServiceId(item.getServiceId());
        serviceSecretInfo.setServiceName(item.getServiceName());
        serviceSecretInfo.setServiceSecret(item.getServiceSecret());

        msbConfigClient.syncServiceItemInfo(serviceSecretInfo);

        return newItem;
    }

    public boolean checkServiceItemDuplicate(String serviceName, String serviceId) {
        return serviceItemRepository.countByServiceIdAndServiceName(serviceId.toLowerCase(),
                serviceId.toUpperCase(), serviceName) > 0;
    }

    public boolean checkServiceCodeDuplicate(String serviceCode){
        List<ServiceItem> items = serviceItemRepository.findByServiceCodeEquals(serviceCode);
        if(CollectionUtils.isEmpty(items)){
            return false;
        }
        return true;
    }


    public ServiceItem findByServiceId(String serviceId) {
        return this.serviceItemRepository.findOneByServiceIdEquals(serviceId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRefreshDate(String serviceId) {
        serviceItemRepository.updateRefreshDate(serviceId, new Date());
    }

    public void delServiceItem(Integer id) {

        ServiceItem serv = serviceItemRepository.findOne(id);
        if (serv == null) {
            throw new MsbException("0006-10013");
        }

        List<ErrorCode> errorCodes = errorCodeService.findAllInService(serv.getServiceId());
        if (errorCodes != null && !errorCodes.isEmpty()) {
            throw new MsbException("0006-10023");
        }

        //是否有api订阅信息
        Response<List<API>> apiCallResult;
        try {
            apiCallResult = msbApiClient.getServiceApis(serv.getServiceId());
        } catch (Exception e) {
            log.error("调用api服务异常,{}", e.getMessage());
            throw new MsbException("0006-10016");
        }
        if (Constant.SUCCESS_RETURN_CODE.equals(apiCallResult.getCode())) {
            if (apiCallResult.getContent().size() > 0) {
                throw new MsbException("0006-10014");
            }
        } else {
            throw new MsbException("0006-10015", apiCallResult.getMessage());
        }


        //是否配置有路由信息
        Response<List<Route>> routeCallResult = null;
        try {
            routeCallResult = msbRouteClient.getServiceRoutes(serv.getServiceId());
        } catch (Exception e) {
            log.error("调用route服务异常,{}", e.getMessage());
            throw new MsbException("0006-10019", e.getMessage());
        }

        if (Constant.SUCCESS_RETURN_CODE.equals(routeCallResult.getCode())) {
            if (routeCallResult.getContent().size() > 0) {
                throw new MsbException("0006-10017");
            }
        } else {
            throw new MsbException("0006-10018", routeCallResult.getMessage());
        }


//        是否有服务配置信息
        Response<List<Config>> configCallResult;
        try {
            configCallResult = msbConfigClient.getServiceConfigs(serv.getServiceId());
        } catch (Exception e) {
            log.error("调用config服务异常,{}", e.getMessage());
            throw new MsbException("0006-10022", e.getMessage());
        }
        if (Constant.SUCCESS_RETURN_CODE.equals(configCallResult.getCode())) {
            if (configCallResult.getContent().size() > 0) {
                throw new MsbException("0006-10020");
            }
        } else {
            throw new MsbException("0006-10021", configCallResult.getMessage());
        }


        this.serviceItemRepository.delete(id);
    }

    public void updateServiceItem(Integer id, ServiceItemVo param) {

        Assert.notNull(param, "0006-00013");

        ServiceItem serv = serviceItemRepository.findOne(id);
        if (serv == null) {
            throw new MsbException("0006-00014");
        }

        if (!StringUtils.isEmpty(param.getServiceName())) {
            serv.setServiceName(param.getServiceName());
            serv.setUpdateTime(new Date());
            serviceItemRepository.save(serv);
        }

    }

}
