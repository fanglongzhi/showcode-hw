package com.gmcc.msb.common.service.client;

import com.gmcc.msb.common.entity.Config;
import com.gmcc.msb.common.entity.ServiceSecretInfo;
import com.gmcc.msb.common.vo.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Yuan Chunhai
 * @Date 10/18/2018-5:19 PM
 */
@FeignClient(name = "msb-config")
public interface MsbConfigClient {

    /**
     * 获取服务微服务配置信息
     * @param serviceId 微服务ID
     * @return
     */
    @GetMapping("/application/{application}/serviceConfig")
    Response<List<Config>> getServiceConfigs(@PathVariable("application") String serviceId);

    /**
     * 将服务密钥信息同步到Config服务器
     * @param serviceItem
     * @return
     */
    @PostMapping("/serviceConfig/serviceItem")
    Response syncServiceItemInfo(@RequestBody ServiceSecretInfo serviceItem);


    @GetMapping("/serviceConfig/signKeyConfig")
    Response<String> getServiceSignKeyConfig(@RequestParam("serviceId") String serviceId, @RequestParam("profile") String profile);

    @GetMapping("/serviceConfig/signKeyConfigs")
    Response<Map<String, String>> getServiceSignKeyConfigs(@RequestParam("profile") String profile);

}
