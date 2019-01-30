package com.gmcc.msb.common.controller;

import com.gmcc.msb.common.vo.Response;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yuan Chunhai
 * @Date 12/7/2018-2:56 PM
 */
@RestController
public class MsbTestController {

    @RequestMapping("/msb-test/{id}")
    public Response<String> test(@PathVariable("id") String id){
        return Response.ok(id);
    }

}
