package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.entity.role.Role;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.service.DataOrgService;
import com.gmcc.msb.msbsystem.service.RoleService;
import com.gmcc.msb.msbsystem.vo.req.role.CreateRoleParam;
import com.gmcc.msb.msbsystem.vo.req.role.ModifyRoleAuthParam;
import com.gmcc.msb.msbsystem.vo.req.role.ModifyUserRoleParam;
import com.gmcc.msb.msbsystem.vo.req.role.QueryRoleParam;
import com.gmcc.msb.msbsystem.vo.resp.AuthorityVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(value = "RoleController", tags = {"角色管理"})
@RestController
@RequestMapping
public class RoleController {
    private static final Logger log = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;
    @Autowired
    private DataOrgService dataOrgService;

    @PostMapping("/role")
    @ApiOperation(value = "添加角色信息", notes = "添加一个角色信息")
    public Result createRole(@RequestBody @Valid CreateRoleParam param, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("数据约束验证失败:{}", bindingResult.getFieldErrors());
            throw new MsbException("0007-00007", "角色名称为空或超长");
        }

        if (!roleService.isRoleNameUnique(param.getRoleName())) {
            throw new MsbException("0007-10025", "角色名称重复");
        }

        Role role = new Role();
        role.setRoleName(param.getRoleName());
        roleService.createRole(role);

        return Result.success();

    }

    @GetMapping("/roles/search")
    @ApiOperation(value = "查询角色信息列表", notes = "查询角色信息列表")
    public Result<List<Role>> findRoleList(@RequestParam(name = "id", required = false) @ApiParam("角色主键") Long id,
                                           @RequestParam(name = "roleName", required = false) @ApiParam("角色名称") String roleName) {

        QueryRoleParam role = new QueryRoleParam();
        role.setId(id);
        role.setRoleName(roleName);
        List<Long> ordIds = UserContextHolder.getContext().getDataOrgList();
        role.setOrgIds(ordIds);
        if(ordIds==null||ordIds.size()==0){
            return Result.success(new ArrayList<Role>());
        }
        return Result.success(roleService.findRoleList(role));
    }

    @PutMapping("/role/{id}")
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @ApiImplicitParam(name = "id", value = "角色ID", paramType = "path", required = true, dataType = "Long")
    public Result modifyRoleName(@PathVariable("id") Long id, @RequestBody @Validated Role role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("数据约束验证失败:{}", bindingResult.getFieldErrors());
            throw new MsbException("0007-00008", "角色名称为空或超长");
        }
        if (!roleService.isRoleNameUnique(role.getRoleName(), id)) {
            throw new MsbException("0007-00009", "角色名称重复");
        }

        role.setId(id);

        roleService.modifyRoleName(role);

        return Result.success();
    }

    @DeleteMapping("/role/{id}")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "id", value = "角色ID", paramType = "path", required = true, dataType = "Long")
    public Result deleteRole(@PathVariable("id") Long id) {
        if (roleService.hasUserRole(id)) {
            throw new MsbException("0007-10026", "该角色下存在账号，不能删除");
        }
        if (roleService.hasAuth(id)) {
            throw new MsbException("0007-10027", "该角色下存在授权模块，不能删除");
        }
        roleService.deleteRole(id);

        return Result.success();
    }

    @PostMapping("/role/{id}/users")
    @ApiOperation(value = "批量往角色下添加或删除用户", notes = "批量往角色下添加或删除用户")
    @ApiImplicitParam(name = "id", value = "角色ID", paramType = "path", required = true, dataType = "Long")
    public Result managerUserRole(@PathVariable("id") Long id, @RequestBody ModifyUserRoleParam param) {

        if ((param.getAddUserIdList() == null || param.getAddUserIdList().isEmpty())
                && (param.getRemoveUserIdList() == null || param.getRemoveUserIdList().isEmpty())) {
            throw new MsbException("0007-00012", "操作数据为空");
        }
        if (roleService.findRole(id) == null) {
            throw new MsbException("0007-10028", "数据不存在");
        }

        param.setRoleId(id);

        roleService.managerUserRole(param);

        return Result.success();
    }

    @GetMapping("/role/{id}/users")
    @ApiOperation(value = "查询角色下的用户", notes = "查询角色下的用户")
    @ApiImplicitParam(name = "id", value = "角色ID", paramType = "path", required = true, dataType = "Long")
    public Result<List<User>> findUserList(@PathVariable("id") Long id) {
        return Result.success(roleService.findUserList(id));
    }

    @GetMapping("/role/{id}/auths")
    @ApiOperation(value = "查询角色下的权限", notes = "查询角色下的权限")
    @ApiImplicitParam(name = "id", value = "角色ID", paramType = "path", required = true, dataType = "Long")
    public Result<List<AuthorityVo>> findAuthList(@PathVariable("id") Long id) {
        if (roleService.findRole(id) == null) {
            throw new MsbException("0007-10029", "数据不存在");
        }
        return Result.success(roleService.findAuthList(id));
    }

    @PostMapping("/role/{id}/auths")
    @ApiOperation(value = "修改角色下的权限", notes = "修改角色下的权限,会删除不在此权限列表中的旧的权限")
    @ApiImplicitParam(name = "id", value = "角色ID", paramType = "path", required = true, dataType = "Long")
    public Result addAuthList(@PathVariable("id") Long id, @RequestBody ModifyRoleAuthParam param) {
        if (roleService.findRole(id) == null) {
            throw new MsbException("0007-10030", "数据不存在");
        }
        param.setRoleId(id);
        roleService.addAuthList(param);
        return Result.success();
    }

    @DeleteMapping("/role/{id}/auth/{authId}")
    @ApiOperation(value = "删除角色下的权限", notes = "删除角色下的权限")
    public Result deleteRoleModule(@PathVariable("id") @ApiParam("角色主键") Long id,
                                   @PathVariable("authId") @ApiParam("权限主键") Long authId) {

        roleService.deleteRoleModule(id, authId);

        return Result.success();
    }

    @PostMapping("/role/{id}/auth/delete/batch")
    @ApiOperation(value = "批量删除角色下的权限", notes = "批量删除角色下的权限")
    public Result deleteRoleModuleBach(@PathVariable("id") @ApiParam("角色主键") Long id, @RequestBody ModifyRoleAuthParam param) {
        if ((param.getAddAuthId() == null || param.getAddAuthId().isEmpty())) {
            throw new MsbException("0007-10031", "操作数据为空");
        }
        param.setRoleId(id);

        roleService.deleteRoleModuleBatch(param);

        return Result.success();
    }
}
