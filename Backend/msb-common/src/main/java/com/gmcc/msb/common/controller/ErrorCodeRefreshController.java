package com.gmcc.msb.common.controller;

import com.gmcc.msb.common.service.ErrorCodeCacheService;
import com.gmcc.msb.common.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yuan Chunhai
 * @Date 10/16/2018-5:34 PM
 */
@RestController
@CrossOrigin
public class ErrorCodeRefreshController {

    @Autowired
    private ErrorCodeCacheService errorCodeCacheServiceImpl;

    @PostMapping("/refreshErrorCodes")
    public Response refresh() {
        errorCodeCacheServiceImpl.refreshCache();
        return Response.ok();
    }

}
