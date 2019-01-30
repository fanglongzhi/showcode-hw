package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.common.CommonConstant;
import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.entity.module.Module;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.service.ModuleService;
import com.gmcc.msb.msbsystem.vo.resp.ModuleRespVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gmcc.msb.msbsystem.common.CommonConstant.ERROR_MODULE_STATUS;

/**
 * @program: msb-system
 * @description: 模块管理
 * @author: zhifanglong
 * @create: 2018-10-10 18:08
 */
@RestController
@Api(value = "ModuleController", tags = {"模块管理"})
public class ModuleController {
    private static final Logger log = LoggerFactory.getLogger(ModuleController.class);
    @Autowired
    private ModuleService moduleService;

    @GetMapping("/module/all")
    @ApiOperation(value = "查询所有模块", notes = "查询所有模块")
    public Result<List<Module>> findAllModule(){
       return Result.success(moduleService.findAllModule());
    }
    @PutMapping("/module/{id}/{status}")
    @ApiOperation(value = "修改模块状态", notes = "修改模块状态")
    public Result<Module> modifyModuleStatus(@PathVariable("id") @ApiParam("主键") Long id,
                                       @PathVariable("status") @ApiParam("状态，0生效，1失效") String status
                                       ){
        if(!(Lists.newArrayList(CommonConstant.MODULE_VALID,CommonConstant.MODULE_INVALID).contains(status))){
            throw new MsbException(ERROR_MODULE_STATUS);
        }
        return Result.success(moduleService.modifyModuleStatus(id,status));
    }

    @GetMapping("/module/{id}/users")
    @ApiOperation(value = "查询模块下的用户", notes = "查询模块下的用户")
    public Result<List<User>> findUserListInModule(@PathVariable("id") @ApiParam("主键") Long id){
        return Result.success(moduleService.findUserListInModule(id));
    }

    @GetMapping("/module/tree")
    @ApiOperation(value = "查询模块树", notes = "查询模块树")
    public Result<ModuleRespVo> findMoudleTree(){
        return Result.success(moduleService.findAllModuleTree());
    }
    @PostMapping("/module/batch/{status}")
    @ApiOperation(value = "批量修改模块状态", notes = "批量修改模块状态")
    public Result<Module> modifyModuleStatusBatch(@RequestBody @ApiParam("模块ID集合") List<Long> moduleIds,
            @PathVariable("status") @ApiParam("状态，0生效，1失效") String status
    ){
        if(!(Lists.newArrayList(CommonConstant.MODULE_VALID,CommonConstant.MODULE_INVALID).contains(status))){
            throw new MsbException(ERROR_MODULE_STATUS);
        }

        if(moduleIds==null||moduleIds.size()==0){
            return Result.success();
        }

        moduleService.modifyModuleStatusBatch(moduleIds,status);
        return Result.success();
    }


}
