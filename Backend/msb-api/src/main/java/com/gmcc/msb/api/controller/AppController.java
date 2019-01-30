package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.common.CommonConstant;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.service.AppService;
import com.gmcc.msb.api.vo.request.AppRequest;
import com.gmcc.msb.api.vo.request.ModifyAppRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.vo.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping
public class AppController {
    private static final Logger log = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private AppService appService;

    @PostMapping("/app")
    @ApiOperation(value = "创建应用")
    public Response createApplication(@RequestBody @Valid @ApiParam AppRequest app, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG, bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_REQUEST_DATA_CODE);
        }

        if (appService.countAppName(app.getAppName()) > 0) {
            throw new MsbException(CommonConstant.ERROR_APP_EXIST_CODE);
        }

        App newApp = appService.createApplication(app);

        return Response.ok(newApp);
    }

    @GetMapping("/app/list/all")
    @ApiOperation("获取应用列表")
    public Response getAppList() {
        List<App> result = appService.findAllApp();
        return Response.ok(result);
    }

    @GetMapping("/app/{id}")
    @ApiOperation("获取应用")
    public Response getApp(@PathVariable("id") @ApiParam("应用ID") Integer id) {
        return Response.ok(appService.findOneApp(id));
    }

    @DeleteMapping("/app/{id}")
    @ApiOperation("删除应用")
    public Response deleteApp(@PathVariable("id") Integer id) {
        appService.deleteApp(id);
        return Response.ok();
    }

    @PostMapping("/app/{id}/apply")
    @ApiOperation("应用审核")
    public Response applyApp(@PathVariable("id") @ApiParam("应用ID") Integer id) {
        appService.applyApp(id);
        return Response.ok();
    }

    @PostMapping("/app/{id}")
    @ApiOperation("修改应用")
    public Response modifyApp(@PathVariable("id") @ApiParam("应用ID") Integer id, @RequestBody @Valid @ApiParam("请求体") ModifyAppRequest param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG, bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_REQUEST_DATA_CODE);
        }

        if (appService.countAppName(param.getAppName(), id) > 0) {
            throw new MsbException(CommonConstant.ERROR_APP_EXIST_CODE);
        }

        return Response.ok(appService.modifyApp(id, param));
    }

    @GetMapping("/app/{id}/apis")
    @ApiOperation("查询应用订阅的api")
    public Response findApi(@PathVariable("id") Integer id) {
        return Response.ok(appService.findApiOfAppSubscribe(id));
    }


    @GetMapping("/apps/queryByIds")
    @ApiOperation(value = "批量查询app", hidden = true)
    public Response<Map<Integer, App>> query(@RequestParam("ids") List<Integer> ids) {
        List<App> apps = appService.findByids(ids);
        Map<Integer, App> map = new HashMap<>(10);
        for (App app : apps) {
            map.put(app.getId(), app);
        }
        return Response.ok(map);
    }

}
