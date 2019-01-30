package com.gmcc.msb.msbservice.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbservice.common.resp.Result;
import com.gmcc.msb.msbservice.entity.ErrorCode;
import com.gmcc.msb.msbservice.service.ErrorCodeService;
import com.gmcc.msb.msbservice.vo.req.ErrorCodeReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 错误码
 *
 * @author Yuan Chunhai
 * @Date 10/12/2018-2:18 PM
 */
@Api(value = "错误码管理")
@RestController
public class ErrorCodeController {

    @Autowired
    private ErrorCodeService errorCodeService;

    @PostMapping("/errorCode")
    @ApiOperation(value = "新增错误码")
    public Result<ErrorCode> add(@RequestBody @ApiParam ErrorCodeReq errorCode) {
        ErrorCode saved = errorCodeService.add(errorCode);
        return Result.success(saved);
    }

    @PutMapping("/errorCode/{id}")
    @ApiOperation(value = "修改错误码")
    public Result<ErrorCode> update(@RequestBody @ApiParam ErrorCodeReq errorCode
            , @PathVariable @ApiParam("id") int id) {
        if (errorCode == null || StringUtils.isEmpty(errorCode.getMessage())) {
            throw new MsbException("0006-00012");
        }
        return Result.success(errorCodeService.update(id, errorCode));
    }


    @DeleteMapping("/errorCode/{id}")
    @ApiOperation(value = "删除错误码")
    public Result delete(@PathVariable @ApiParam("id") int id) {
        this.errorCodeService.delete(id);
        return Result.success();
    }

    @GetMapping("/errorCode/{id}")
    @ApiOperation(value = "查询错误码")
    public Result<ErrorCode> find(@PathVariable @ApiParam("id") int id) {
        return Result.success(this.errorCodeService.find(id));
    }

    @GetMapping("/errorCodes")
    @ApiOperation(value = "搜索错误码")
    public Result<Page<ErrorCode>> query(@PageableDefault(sort = "code") Pageable pageable) {
        return Result.success(this.errorCodeService.findAll(pageable));
    }

    @GetMapping("/errorCodes/{serviceId}")
    @ApiOperation(value = "查询为服务下所有错误码")
    public Result<ErrorCode> query(@PathVariable String serviceId) {
        return Result.success(this.errorCodeService.findAllInService(serviceId));
    }


    @PostMapping("/errorCodes/{serviceId}/refresh")
    @ApiOperation(value = "刷新微服务")
    public Result refreshCache(@PathVariable String serviceId) {
        errorCodeService.refreshsCache(serviceId);
        return Result.success();
    }

}
