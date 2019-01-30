package com.gmcc.msb.common.service.client;

import com.gmcc.msb.common.vo.Response;
import com.gmcc.msb.common.vo.request.TaskLogRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @program: msb-common
 * @description: 系统服务访问客户端
 * @author: zhifanglong
 * @create: 2018-10-24 16:09
 */
@FeignClient(name = "msb-system")
public interface MsbSystemClient {

    /**
     * 获取主组
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}/dataOrg/main")
    Response<Long> findMainOrgId(@PathVariable("userId") String userId);

    /**
     * 获取用户组信息，包括主组和可见组信息，组组key mainOrg,其他 dataOrg
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}/dataOrg/all")
    Response<Map<String, List<Long>>> findAllOrgId(@PathVariable("userId") String userId);



    @PostMapping("/tasklog")
    Response<String> addTaskLog(@RequestBody TaskLogRequest taskLogRequest);

}
