package com.gmcc.msb.common.service.client;

import com.gmcc.msb.common.entity.ErrorCode;
import com.gmcc.msb.common.vo.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 10/12/2018-2:18 PM
 */
@FeignClient(name = "msb-service")
public interface MsbServiceClient {

    /**
     * 获取微服务所有错误码
     * @param serviceId
     * @return
     */
    @GetMapping("/errorCodes/{serviceId}")
    Response<List<ErrorCode>> findAllInService(@PathVariable(name = "serviceId") String serviceId);

    /**
     * 刷新时间通知
     *
     * @param serviceId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/serviceItem/{serviceId}/updateRefreshData",
            consumes = "application/json")
    Response updateRefreshData(@PathVariable("serviceId") String serviceId);

}
