package com.gmcc.msb.msbservice.controller;

import com.gmcc.msb.msbservice.common.resp.Result;
import com.gmcc.msb.msbservice.service.ServiceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author zhi fanglong
 */
@RestController
public class ServiceLogController {


    @Autowired
    ServiceLogService serviceLogService;

    @GetMapping("/register_log")
    public Result hello(@PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable){
        return Result.success(serviceLogService.find(pageable));
    }
}
