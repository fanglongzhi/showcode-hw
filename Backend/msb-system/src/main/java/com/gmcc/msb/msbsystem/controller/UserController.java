package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.common.CommonConstant;
import com.gmcc.msb.msbsystem.common.UserStatus;
import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.service.UserService;
import com.gmcc.msb.msbsystem.vo.req.user.CreateUserParam;
import com.gmcc.msb.msbsystem.vo.req.user.QueryUserParam;
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

@Api(value = "UserController", tags = {"账户数据管理"})
@RestController
@RequestMapping
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @ApiOperation(value = "添加开发者账户信息", notes = "添加一个账户信息")
    @PostMapping("/user")
    public Result createUser(@RequestBody @Valid @ApiParam CreateUserParam param,
                             BindingResult bindingResult ) throws Exception{

        if(bindingResult.hasErrors()){
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG,bindingResult.getFieldErrors());
            throw new MsbException("0007-00005", CommonConstant.ERROR_VALIDATE_MSG);
        }
        User user = new User(param);
        user.setId(null);
        user.setIsLock(false);
        user.setStatus(UserStatus.VALID);
        return userService.saveUser(user);
    }

    @ApiOperation(value = "查询开发者账户信息", notes = "根据ID查询开发者账户信息")
    @ApiImplicitParam(name = "id", value = "账户ID", paramType = "path", required = true, dataType = "Long")
    @GetMapping("/user/{id}")
    public Result<User> findUser(@PathVariable("id") Long id) {
        return userService.findOneUser(id);
    }

    @GetMapping("/users/search")
    @ApiOperation(value = "查询用户信息列表", notes = "根据用户编号，用户姓名，账户状态，账户锁定状态过滤用户列表信息")
    public Result<List<User>> findUserList(@RequestParam(name = "operatorId", required = false)@ApiParam("用户编号") String operatorId,
                                           @RequestParam(name = "name", required = false) @ApiParam("用户姓名") String name,
                                           @RequestParam(name = "status", required = false) @ApiParam("状态") UserStatus status,
                                           @RequestParam(name = "isLock", required = false) @ApiParam("锁定状态") Boolean isLock

    ) {
        QueryUserParam param = new QueryUserParam();
         param.setIsLock(isLock);
         param.setName(name);
         param.setStatus(status);
         param.setOperatorId(operatorId);

        return userService.findUserListWithDataOrgInfo(param);

    }

    @PutMapping("/user/{id}")
    @ApiOperation(value = "对账户进行锁定，解锁，可用，不可用设置", notes = "对账户进行锁定，解锁，可用，不可用设置")
    @ApiImplicitParam(name = "id", value = "账户ID", paramType = "path", required = true, dataType = "Long")
    public Result modifyUser(@PathVariable("id") Long id,@RequestBody @ApiParam QueryUserParam.ModifyUserParam param){
        param.setId(id);
        if(param.getStatus()!=null){
            return userService.modifyUserStatus(param);
        }

        if(param.getIsLock()!=null){
            return userService.modifyUserLock(param);
        }

        throw new MsbException("0007-00006", "缺少参数");
    }

    @GetMapping("/user/token/check_token")
    @ApiOperation(value = "对登陆用户的token进行验证", notes = "访问SSO验证用户TOKEN")
    public Result checkToken(@RequestParam(name = "token", required = true)@ApiParam("sso token") String token){
      return userService.checkSSOToken(token);
    }
}
