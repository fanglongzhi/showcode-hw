package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppAudit;
import com.gmcc.msb.api.service.AppAuditService;
import com.gmcc.msb.api.vo.request.AuditRequest;
import com.gmcc.msb.common.vo.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AppAuditController {

    @Autowired
    private AppAuditService appAuditService;

    @PostMapping("/app/audit/{auditId}")
    @ApiOperation("审核应用")
    public Response<App> audit(@PathVariable @ApiParam("审核ID") Integer auditId, @Valid @RequestBody @ApiParam AuditRequest vo) {
        App result = this.appAuditService.audit(auditId, vo);
        return new Response(result);
    }

    @GetMapping("/app/audit/list")
    @ApiOperation("获取审核列表")
    public Response<Page<AppAudit>> getAuditList(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AppAudit> page = this.appAuditService.findAll(pageable);
        return Response.ok(page);
    }

}
