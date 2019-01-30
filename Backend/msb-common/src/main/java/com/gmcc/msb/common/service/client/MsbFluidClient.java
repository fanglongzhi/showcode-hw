package com.gmcc.msb.common.service.client;

import com.gmcc.msb.common.vo.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: msb-common
 * @description: 留空服务调用接口
 * @author: zhifanglong
 * @create: 2018-11-07 13:42
 */
@FeignClient(name = "msb-fluid")
public interface MsbFluidClient {
    @GetMapping("/strategy/{type}/{objectId}/exist")
    public Response<Boolean> judgeStrategyExist(@PathVariable("type")String type,
                                       @PathVariable("objectId")Integer objectId);
}
