package com.gmcc.msb.msbservice.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbservice.common.resp.CommonConstant;
import com.gmcc.msb.msbservice.common.resp.Result;
import com.gmcc.msb.msbservice.entity.ServiceItem;
import com.gmcc.msb.msbservice.service.ServiceItemService;
import com.gmcc.msb.msbservice.vo.ServiceItemVo;
import com.gmcc.msb.msbservice.vo.ServiceStatusResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zhi fanglong
 */
@Api(value = "ServiceItemController", tags = {"服务目录管理"})
@RestController
@RequestMapping
public class ServiceItemController {
    private static final Logger log = LoggerFactory.getLogger(ServiceItemController.class);
    @Autowired
    private ServiceItemService serviceItemService;

    @GetMapping("/serviceItem/all")
    @ApiOperation(value = "查询服务目录列表", notes = "查询服务目录列表")
    public Result<List<ServiceItem>> findServiceItemAll() {
        return Result.success(serviceItemService.findServiceItem());
    }

    @GetMapping("/serviceItem/{id}/status")
    @ApiOperation(value = "查询服务实例信息", notes = "查询服务实例信息")
    @ApiImplicitParam(name = "id", value = "服务目录主键", paramType = "path", required = true, dataType = "Integer")
    public Result<List<ServiceStatusResp>> findServiceStatus(@PathVariable("id") Integer id) {
        return Result.success(serviceItemService.findServiceStatus(id));
    }

    @PostMapping("/serviceItem/sync")
    @ApiOperation(value = "同步服务目录", notes = "从注册服务器同步服务目录")
    public Result syncServiceList() {
        return Result.success(serviceItemService.syncServiceList());
    }

    @PostMapping("/serviceItem")
    @ApiOperation(value = "创建服务目录", notes = "手动创建一个服务目录")
    public Result createServiceItem(@Valid @RequestBody @ApiParam  ServiceItemVo param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG, bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_VALIDATE_CODE, CommonConstant.ERROR_VALIDATE_MSG);
        }

        this.checkServiceId(param.getServiceId());

        if (serviceItemService.checkServiceItemDuplicate(param.getServiceName(), param.getServiceId())) {
            throw new MsbException(CommonConstant.ERROR_DUPLICATE_CODE, CommonConstant.ERROR_DUPLICATE_MSG);
        }

        serviceItemService.createServiceItem(param);

        return Result.success();
    }

    @PutMapping("/serviceItem/{id}")
    @ApiOperation(value = "修改服务目录", notes = "修改服务目录")
    public Result createServiceItem(@PathVariable Integer id, @RequestBody @ApiParam ServiceItemVo param) {
        serviceItemService.updateServiceItem(id, param);
        return Result.success();
    }


    @DeleteMapping("/serviceItem/{id}")
    @ApiOperation(value = "删除服务目录", notes = "手动删除一个服务目录")
    public Result createServiceItem(@PathVariable Integer id) {
        serviceItemService.delServiceItem(id);
        return Result.success();
    }


    @PostMapping("/serviceItem/{serviceId}/updateRefreshData")
    @ApiOperation(value = "更新服务的配置成功刷新时间", notes = "更新服务的配置成功刷新时间")
    public Result updateRefreshData(@PathVariable("serviceId") @ApiParam("服务ID") String serviceId){
       serviceItemService.updateRefreshDate(serviceId);

       return Result.success();
    }

    private void checkServiceId(String serviceId) {
        if (!serviceId.matches("[a-zA-Z0-9_\\-]+")) {
            throw new MsbException("0006-00015");
        }
    }
}
