package com.gmcc.msb.common.service.client;

import com.gmcc.msb.common.entity.Config;
import com.gmcc.msb.common.entity.Route;
import com.gmcc.msb.common.vo.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 10/18/2018-6:05 PM
 */
@FeignClient("msb-route")
public interface MsbRouteClient {

    /**
     * 获取服务路由配置
     * @param serviceId 服务id
     * @return
     */
    @GetMapping("/routes/{serviceId}")
    Response<List<Route>> getServiceRoutes(@PathVariable("serviceId") String serviceId);


}
