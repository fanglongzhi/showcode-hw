package com.gmcc.msb.common.service.client;

import com.gmcc.msb.common.entity.API;
import com.gmcc.msb.common.entity.App;
import com.gmcc.msb.common.vo.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Yuan Chunhai
 * @Date 10/18/2018-5:16 PM
 */
@FeignClient(name = "msb-api")
public interface MsbApiClient {

    /**
     * 获取服务api
     *
     * @param serviceId
     * @return
     */
    @GetMapping("/apis/{serviceId}")
    Response<List<API>> getServiceApis(@PathVariable("serviceId") String serviceId);


    /**
     * 通过ids查询api
     *
     * @param ids id集合
     * @return
     */
    @GetMapping("/apis/queryByIds")
    Response<Map<Integer, API>> getApisById(@RequestParam("ids") Set<Integer> ids);

    /**
     * 通过ids查询api
     *
     * @param ids id集合
     * @return
     */
    @GetMapping("/apps/queryByIds")
    Response<Map<Integer, App>> getAppsById(@RequestParam("ids") Set<Integer> ids);

}
