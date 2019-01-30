package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.ApiAudit;
import com.gmcc.msb.api.service.ApiService;
import com.gmcc.msb.api.service.SyncRedisService;
import com.gmcc.msb.api.vo.request.ApiRequest;
import com.gmcc.msb.api.vo.request.AuditRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.vo.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "API操作")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @Autowired
    private SyncRedisService syncRedisService;

    @PostMapping("/api")
    @ApiOperation(value = "新增API")
    public Response<API> add(@RequestBody @Valid @ApiParam(value = "请求内容") ApiRequest apiRequest) {
        API api = new API();
        BeanUtils.copyProperties(apiRequest, api);
        API saved = this.apiService.add(api);
        this.apiService.setServiceName(saved);
        return new Response(saved);
    }

    @PutMapping("/api/{id}")
    @ApiOperation(value = "更新API")
    public Response<API> update(@PathVariable @ApiParam("API id") Integer id, @RequestBody @ApiParam("请求体") ApiRequest apiRequest) {
        API api = new API();
        BeanUtils.copyProperties(apiRequest, api);
        API updated = this.apiService.update(id, api);
        this.apiService.setServiceName(updated);
        return new Response(updated);
    }

    @DeleteMapping("/api/{id}")
    @ApiOperation("删除API")
    public Response delete(@PathVariable @ApiParam("API id") Integer id) {
        this.apiService.delete(id);
        return new Response();
    }

    @GetMapping("/api/{id}")
    @ApiOperation("查询API")
    public Response<API> get(@PathVariable @ApiParam("API id") Integer id) {
        API find = this.apiService.findOne(id);
        if (find == null) {
            throw new MsbException("0001-10129");
        }
        this.apiService.setServiceName(find);
        return new Response(find);
    }

    @GetMapping("/apis")
    @ApiOperation("获取API列表")
    public Response<Page<API>> get(
            @RequestParam(required = false) @ApiParam(value = "服务ID") String serviceId,
            @RequestParam(required = false) @ApiParam(value = "服务名") String serviceName,
            @RequestParam(required = false) @ApiParam(value = "API名") String apiName,
            @RequestParam(required = false) @ApiParam(value = "API状态") Integer status,
            Pageable pageable) {
        Page<API> page = this.apiService.find(serviceId, serviceName, apiName, status, pageable);
        setPageServiceNames(page);
        return new Response(page);
    }

    @GetMapping("/apis/all")
    @ApiOperation("获取API列表")
    public Response<Page<API>> getAllApi(
            @RequestParam(required = false) @ApiParam(value = "服务ID") String serviceId,
            @RequestParam(required = false) @ApiParam(value = "服务名") String serviceName,
            @RequestParam(required = false) @ApiParam(value = "API名") String apiName,
            @RequestParam(required = false) @ApiParam(value = "API状态") Integer status,
            Pageable pageable) {
        Page<API> page = this.apiService.findAll(serviceId, serviceName, apiName, status, pageable);
        setPageServiceNames(page);
        return new Response(page);
    }

    @ApiOperation(value = "获取服务下API列表", hidden = true)
    @GetMapping("/apis/{serviceId}")
    public Response<List<API>> getServiceApis(@PathVariable String serviceId) {
        List<API> result = this.apiService.findAllServiceApis(serviceId);
        return Response.ok(result);
    }

    private void setPageServiceNames(Page<API> all) {
        Map<String, String> cache = new HashMap<>(20);

        if (all.getContent() != null) {
            for (API api : all.getContent()) {
                if (cache.containsKey(api.getServiceId())) {
                    api.setServiceName(cache.get(api.getServiceId()));
                } else {
                    this.apiService.setServiceName(api);
                    cache.put(api.getServiceId(), api.getServiceName());
                }
            }
        }
    }

    @PostMapping("/api/{id}/apply/{type}")
    @ApiOperation("API申请")
    public Response<API> apply(@PathVariable @ApiParam("API id") Integer id, @PathVariable @ApiParam("0上线申请，1下线申请") Integer type) {
        API result = this.apiService.apply(id, type);
        this.apiService.setServiceName(result);
        return new Response(result);
    }

    @PostMapping("/api/audit/{auditId}")
    @ApiOperation("API审核")
    public Response<API> audit(@PathVariable @ApiParam("审核ID") Integer auditId, @Valid @RequestBody @ApiParam() AuditRequest vo) {
        API result = this.apiService.audit(auditId, vo);
        this.apiService.setServiceName(result);
        return new Response(result);
    }

    @GetMapping("/api/audit/list")
    @ApiOperation("获取API审核")
    public Response<Page<ApiAudit>> getAuditList(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ApiAudit> page = this.apiService.getAuditList(pageable);
        return Response.ok(page);
    }

    @GetMapping("/api/{id}/apps")
    @ApiOperation(value = "获取订阅了API的应用")
    public Response<Page<Map<String, Object>>> apiUsedByApp(@PathVariable @ApiParam("APP id") Integer id, Pageable pageable) {
        Page<Map<String, Object>> page = this.apiService.appsThatOrderApiId(id, pageable);
        return new Response(page);
    }


    @GetMapping("/apis/queryByIds")
    @ApiOperation(value = "批量查询api", hidden = true)
    public Response<Map<Integer, API>> query(@RequestParam("ids") List<Integer> ids) {
        List<API> apps = apiService.findByids(ids);
        Map<Integer, API> map = new HashMap<>(10);
        for (API api : apps) {
            map.put(api.getId(), api);
        }
        return Response.ok(map);
    }


    @GetMapping("/api/sync")
    @ApiOperation("API同步")
    public Response sync() {
        syncRedisService.syncApiInfo();
        syncRedisService.syncApps();
        syncRedisService.syncAppOrderApis();
        return Response.ok();
    }

}
