package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.entity.org.DataOrg;
import com.gmcc.msb.msbsystem.entity.org.DataOrgUser;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.service.DataOrgService;
import com.gmcc.msb.msbsystem.vo.req.org.CreateDataOrgParam;
import com.gmcc.msb.msbsystem.vo.req.org.CreateDataOrgUserParam;
import com.gmcc.msb.msbsystem.vo.req.org.ModifyDataOrgParam;
import com.gmcc.msb.msbsystem.vo.resp.DataOrgRespVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: msb-system
 * @description: 用户数据组控制类
 * @author: zhifanglong
 * @create: 2018-10-24 16:00
 */
@Api(value = "DataOrgController", tags = {"用户数据组管理"})
@RestController
@RequestMapping
public class DataOrgController {
    private static final Logger log = LoggerFactory.getLogger(DataOrgController.class);
    private static final String DATA_INVALID_FAIL="0007-00010";
    @Autowired
    private DataOrgService dataOrgService;

    @GetMapping("/user/{userId}/dataOrg/main")
    public Result<Long> findMainOrgId(@PathVariable("userId") String userId) {
        List<DataOrgUser> orgList = dataOrgService.findUserMainDataOrg(userId);
        if (orgList != null && orgList.size() > 0) {
            return Result.success(orgList.get(0).getOrgId());
        }

        return Result.success();
    }

    @GetMapping("/user/{userId}/dataOrg/all")
    public Result<Map<String, List<Long>>> findAllOrgId(@PathVariable("userId") String userId) {
        List<Long> parentList = dataOrgService.findOrgIdsOfUser(userId);
        List<Long> children = Lists.newArrayList();
        if (parentList != null && parentList.size() > 0) {
            children = dataOrgService.findOrgChildrenList(parentList);
        }
        Set<Long> idList = Sets.newHashSet();
        if (parentList != null) {
            idList.addAll(parentList);
        }
        idList.addAll(children);

        Long mainId = null;

        List<DataOrgUser> orgMainList = dataOrgService.findUserMainDataOrg(userId);
        if (orgMainList != null && orgMainList.size() > 0) {
            mainId = Long.valueOf(orgMainList.get(0).getOrgId().longValue());
        }

        Map<String, List<Long>> result = new HashMap<>();
        if (mainId != null) {
            result.put("mainOrg", Lists.newArrayList(mainId));
        }
        result.put("dataOrg", Lists.newArrayList(idList));

        return Result.success(result);
    }

    @PostMapping("/dataOrg")
    @ApiOperation(value = "添加数据组信息", notes = "添加一个数据组信息")
    public Result<DataOrg> createDataOrg(@RequestBody @Valid @ApiParam CreateDataOrgParam param, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("数据约束验证失败:{}", bindingResult.getFieldErrors());
            throw new MsbException(DATA_INVALID_FAIL,"缺少父节点或组名");
        }
        return Result.success(dataOrgService.createDataOrg(param));
    }
    @PutMapping("/dataOrg/{id}")
    @ApiOperation(value = "修改数据组信息", notes = "修改数据组信息")
    public Result<DataOrg> updateDataOrg(@PathVariable("id")Integer id,@RequestBody @Valid @ApiParam ModifyDataOrgParam param,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("数据约束验证失败:{}", bindingResult.getFieldErrors());
            throw new MsbException(DATA_INVALID_FAIL,"缺少组名");
        }

        return Result.success(dataOrgService.modifyDataOrg(id,param));
    }

    @DeleteMapping("/dataOrg/{id}")
    @ApiOperation(value = "删除数据组信息", notes = "删除数据组信息")
    public Result deleteDataOrg(@PathVariable("id")Integer id){
        dataOrgService.deleteDataOrg(id);

        return Result.success();
    }

    @GetMapping("/dataOrg/tree")
    @ApiOperation(value = "查询数据组数", notes = "查询数据组数")
    public Result<DataOrgRespVo> findTree(){
        return Result.success(dataOrgService.findDataOrgTree());
    }
    @PostMapping("/dataOrgUser/add/batch")
    @ApiOperation(value = "添加用户组关系", notes = "添加用户组关系")
    public Result addDataOrgAuth(@RequestBody @Valid @ApiParam CreateDataOrgUserParam param,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("数据约束验证失败:{}", bindingResult.getFieldErrors());
            throw new MsbException(DATA_INVALID_FAIL,"缺少组");
        }
        dataOrgService.createDataOrgUser(param);
        return Result.success();
    }

    @PostMapping("/dataOrgUser/delete/batch")
    @ApiOperation(value = "删除用户组关系", notes = "删除用户组关系")
    public Result deleteDataOrgAuth(@RequestBody @Valid @ApiParam CreateDataOrgUserParam param,BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            log.error("数据约束验证失败:{}", bindingResult.getFieldErrors());
            throw new MsbException(DATA_INVALID_FAIL,"缺少组");
        }
        dataOrgService.deleteDataOrgUser(param);
        return Result.success();
    }

    @GetMapping("/dataOrg/{orgId}/user/{type}")
    @ApiOperation(value = "查询组下的用户", notes = "查询组下的用户")
    public Result<List<User>> findUserList(@PathVariable("orgId")@ApiParam("组ID") Integer orgId,
                                           @PathVariable("type") @ApiParam("查询类型，1 查询主组用户 0 查询可见组用户 2 查询所有主组用户 3 查询用户所在主组及其子组的主组用户") String type) {


        return Result.success(dataOrgService.findUserList(orgId,type));
    }




}
