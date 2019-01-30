package com.gmcc.msb.msbbreak.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.vo.Response;
import com.gmcc.msb.msbbreak.common.CommonConstant;
import com.gmcc.msb.msbbreak.common.resp.Result;
import com.gmcc.msb.msbbreak.entity.BreakerStrategy;
import com.gmcc.msb.msbbreak.entity.BreakerStrategyLog;
import com.gmcc.msb.msbbreak.entity.ServiceApi;
import com.gmcc.msb.msbbreak.service.BreakerService;
import com.gmcc.msb.msbbreak.service.BreakerStrategyLogService;
import com.gmcc.msb.msbbreak.vo.req.BindBreakerApiBatchParam;
import com.gmcc.msb.msbbreak.vo.req.BindBreakerApiParam;
import com.gmcc.msb.msbbreak.vo.req.ModifyBreakerStrategyParam;
import com.gmcc.msb.msbbreak.vo.resp.ApiResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gmcc.msb.msbbreak.common.CommonConstant.ERROR_API_NOT_EXIST;
import static com.gmcc.msb.msbbreak.common.CommonConstant.ERROR_MISSING_STRATIGY_ID;
import static com.gmcc.msb.msbbreak.common.CommonConstant.ERROR_STRATEGY_NOT_EXIST;

@Api(value = "BreakerController", tags = {"熔断配置管理"})
@RestController
@RequestMapping
public class BreakerController {
    private static final Logger log = LoggerFactory.getLogger(BreakerController.class);
    @Autowired
    private BreakerService breakerService;
    @Autowired
    private BreakerStrategyLogService breakerStrategyLogService;
    @PostMapping("/break/default")
    @ApiOperation(value = "添加一条默认熔断策略", notes = "添加一条默认熔断策略")
    public Result<BreakerStrategy> createBreakerStrategyDefault(@RequestBody @Validated @ApiParam ModifyBreakerStrategyParam param, BindingResult bindingResult) throws MsbException{
        if(bindingResult.hasErrors()){
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG,bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_VALIDATE_CODE, CommonConstant.ERROR_VALIDATE_MSG);
        }

        BreakerStrategy breakerStrategy = new BreakerStrategy();
        breakerStrategy.setId(null);
        breakerStrategy.setEnableBreaker(param.getEnableBreaker());
        breakerStrategy.setFailRate(param.getFailRate());
        breakerStrategy.setRequestVolume(param.getRequestVolume());
        breakerStrategy.setSleep(param.getSleep());
        breakerStrategy.setTimeout(param.getTimeout());
        breakerStrategy.setIsDefault(true);
        breakerStrategy.setStrategyName(param.getStrategyName());

        breakerService.manageBreakStrategy(breakerStrategy);
        log(breakerStrategy, 1);
        return Result.success(breakerStrategy);
    }

    private void log(BreakerStrategy breakerStrategy, int operatorType) {
        log(breakerStrategy,null,operatorType);
    }

    private void log(BreakerStrategy breakerStrategy,String apis, int operatorType) {
        BreakerStrategyLog strategyLog = new BreakerStrategyLog(breakerStrategy);
        strategyLog.setOperatorType(operatorType);
        strategyLog.setCreateTime(breakerStrategy.getCreateTime());
        strategyLog.setUpdateBy(breakerStrategy.getUpdateBy());
        strategyLog.setUpdateTime(breakerStrategy.getUpdateTime());
        strategyLog.setUser(breakerStrategy.getUser());
        strategyLog.setApis(apis);
        breakerStrategyLogService.save(strategyLog);
    }

    @PostMapping("/break/common")
    @ApiOperation(value = "添加一条非默认熔断策略", notes = "添加一条非默认熔断策略")
    public Result<BreakerStrategy> createBreakerStrategyCommon(@RequestBody @Validated @ApiParam ModifyBreakerStrategyParam param, BindingResult bindingResult) throws MsbException{
        if(bindingResult.hasErrors()){
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG,bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_VALIDATE_CODE, CommonConstant.ERROR_VALIDATE_MSG);
        }

        BreakerStrategy breakerStrategy = new BreakerStrategy();
        breakerStrategy.setId(null);
        breakerStrategy.setEnableBreaker(param.getEnableBreaker());
        breakerStrategy.setFailRate(param.getFailRate());
        breakerStrategy.setRequestVolume(param.getRequestVolume());
        breakerStrategy.setSleep(param.getSleep());
        breakerStrategy.setTimeout(param.getTimeout());
        breakerStrategy.setIsDefault(false);
        breakerStrategy.setStrategyName(param.getStrategyName());

        breakerService.manageBreakStrategy(breakerStrategy);
        log(breakerStrategy,1);
        return Result.success(breakerStrategy);
    }

    @PutMapping("/break/default")
    @ApiOperation(value = "修改一条默认熔断策略", notes = "修改一条默认熔断策略")
    public Result<BreakerStrategy> modifyBreakerStrategyDefault(@RequestBody @Validated @ApiParam ModifyBreakerStrategyParam param, BindingResult bindingResult) throws MsbException{
        if(bindingResult.hasErrors()){
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG,bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_VALIDATE_CODE, CommonConstant.ERROR_VALIDATE_MSG);
        }

        BreakerStrategy breakerStrategy = new BreakerStrategy();
        breakerStrategy.setEnableBreaker(param.getEnableBreaker());
        breakerStrategy.setFailRate(param.getFailRate());
        breakerStrategy.setRequestVolume(param.getRequestVolume());
        breakerStrategy.setSleep(param.getSleep());
        breakerStrategy.setTimeout(param.getTimeout());
        breakerStrategy.setIsDefault(true);
        breakerStrategy.setStrategyName(param.getStrategyName());

        breakerService.manageBreakStrategy(breakerStrategy);
        log(breakerStrategy,2);
        return Result.success(breakerStrategy);
    }

    @PutMapping("/break/{id}/common")
    @ApiOperation(value = "修改一条非默认熔断策略", notes = "修改一条非默认熔断策略")
    public Result<BreakerStrategy> modifyBreakerStrategyCommon(@PathVariable("id") Integer id,@RequestBody @Validated @ApiParam ModifyBreakerStrategyParam param, BindingResult bindingResult) throws MsbException{
        if(bindingResult.hasErrors()){
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG,bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_VALIDATE_CODE, CommonConstant.ERROR_VALIDATE_MSG);
        }

        BreakerStrategy breakerStrategy = new BreakerStrategy();
        breakerStrategy.setId(id);
        breakerStrategy.setEnableBreaker(param.getEnableBreaker());
        breakerStrategy.setFailRate(param.getFailRate());
        breakerStrategy.setRequestVolume(param.getRequestVolume());
        breakerStrategy.setSleep(param.getSleep());
        breakerStrategy.setTimeout(param.getTimeout());
        breakerStrategy.setIsDefault(false);
        breakerStrategy.setStrategyName(param.getStrategyName());

        breakerService.manageBreakStrategy(breakerStrategy);
        log(breakerStrategy,2);
        return Result.success(breakerStrategy);
    }
    @GetMapping("/break/{id}/detail")
    @ApiOperation(value = "查询一条非默认熔断策略", notes = "查询一条非默认熔断策略")
    public Result<BreakerStrategy> findBreakerStrategy(@PathVariable("id") Integer id){
        return Result.success(breakerService.findBreakerStrategy(id));
    }

    @GetMapping("/break/default")
    @ApiOperation(value = "查询默认熔断策略", notes = "查询默认熔断策略")
    public Result<BreakerStrategy> findBreakerStrategyDefault(){
        return Result.success(breakerService.findBreakerStrategyDefault());
    }
    @GetMapping("/breaks/search")
    @ApiOperation(value = "查询熔断策略列表", notes = "查询熔断策略列表")
    public Result<List<BreakerStrategy>> findBreakerList(@RequestParam(name = "name", required = false) @ApiParam("策略名称") String name){
        BreakerStrategy breakerStrategy = new BreakerStrategy();
        breakerStrategy.setStrategyName(name);

        return Result.success(breakerService.findAllBreakerStrategy(breakerStrategy));
    }
    @DeleteMapping("/break/{id}")
    @ApiOperation(value = "删除熔断策略", notes = "删除熔断策略")
    public Result deleteBreakerStrategy(@PathVariable("id") Integer id){
        BreakerStrategy breakerStrategy = breakerService.findBreakerStrategy(id);
        if(breakerStrategy ==null){
            throw new MsbException(ERROR_STRATEGY_NOT_EXIST,"熔断策略不存在");
        }
        breakerService.deleteBreaker(id);
        log(breakerStrategy,3);
        return Result.success();
    }
    @PutMapping("/break/{id}/enable")
    @ApiOperation(value = "启用熔断策略", notes = "启用熔断策略")
    public Result enableBreakStrategy(@PathVariable("id") Integer id){
        BreakerStrategy breakerStrategy = breakerService.findBreakerStrategy(id);
        if(breakerStrategy ==null){
            throw new MsbException(ERROR_STRATEGY_NOT_EXIST,"熔断策略不存在");
        }
        breakerService.enableBreaker(id);
        log(breakerStrategy,4);
        return Result.success();
    }
    @PutMapping("/break/{id}/disable")
    @ApiOperation(value = "关闭熔断策略", notes = "关闭熔断策略")
    public Result disableBreakStrategy(@PathVariable("id") Integer id){
        BreakerStrategy breakerStrategy = breakerService.findBreakerStrategy(id);
        if(breakerStrategy ==null){
            throw new MsbException(ERROR_STRATEGY_NOT_EXIST,"熔断策略不存在");
        }
        breakerService.disableBreaker(id);
        log(breakerStrategy,5);
        return Result.success();
    }

    @PostMapping("/serviceApi/{id}/apiStrategy")
    @ApiOperation(value = "为一个API绑定熔断策略", notes = "为一个API绑定熔断策略")
    public Result bindApiStrategy(@PathVariable("id") @ApiParam("API主键") Integer id, @RequestBody @Validated @ApiParam  BindBreakerApiParam param,BindingResult bindingResult) throws MsbException{
        if(bindingResult.hasErrors()){
            log.error(CommonConstant.ERROR_VALIDATE_CODE,bindingResult.getFieldErrors());
            throw new MsbException(ERROR_MISSING_STRATIGY_ID, "缺少策略ID");
        }
        ServiceApi serviceApi = breakerService.findServiceApi(id);
        if(serviceApi==null){
            throw new MsbException(ERROR_API_NOT_EXIST, "api不存在");
        }
        BreakerStrategy breakerStrategy = breakerService.findBreakerStrategy(param.getStrategyId());

        if(breakerStrategy==null){
            throw new MsbException(ERROR_STRATEGY_NOT_EXIST,"熔断策略不存在");
        }

        breakerService.createBreakerStrategyApi(breakerStrategy,serviceApi);
        log(breakerStrategy,String.valueOf(id),6);
        return Result.success();
    }
    @DeleteMapping("/serviceApi/{id}/apiStrategy")
    @ApiOperation(value = "删除一个API绑定熔断策略", notes = "删除一个API绑定熔断策略")
    public Result unbindApiStrategy(@PathVariable("id") @ApiParam("API 主键") Integer id){
        ServiceApi serviceApi = breakerService.findServiceApi(id);
        if(serviceApi==null){
            throw new MsbException(ERROR_API_NOT_EXIST, "api不存在");
        }
        BreakerStrategy breakerStrategy = breakerService.findBreakerStrategyByApiId(id);
        breakerService.deleteBreakerStrategyApi(id);

        log(breakerStrategy,String.valueOf(id),7);
        return Result.success();
    }
    @GetMapping("/serviceApi/{id}/apiStrategy")
    @ApiOperation(value = "查询一个API绑定熔断策略", notes = "查询一个API绑定熔断策略")
    public Result queryApiStrategy(@PathVariable("id") @ApiParam("API 主键")Integer id){
       return Result.success(breakerService.findBreakerStrategyByApiId(id));
    }

    @GetMapping("/break/{id}/serviceApi")
    @ApiOperation(value = "查询API列表", notes = "查询API列表")
    public Result<ApiResp> queryAllApi(@PathVariable("id") @ApiParam("熔断策略主键")Integer id){
        if(breakerService.findBreakerStrategy(id)==null){
            throw new MsbException(ERROR_STRATEGY_NOT_EXIST,"熔断策略不存在");
        }

        return Result.success(breakerService.findApiList(id));
    }

    @PostMapping("/apiStrategy/{id}/serviceApi/add/batch")
    @ApiOperation(value = "批量API绑定熔断策略", notes = "批量API绑定熔断策略")
    public Result bindApiStrategy(@PathVariable("id") @ApiParam("熔断策略主键") Integer id, @RequestBody @ApiParam BindBreakerApiBatchParam param) throws MsbException{
        if(param.getApiIdList().size()==0){
            throw new MsbException(ERROR_MISSING_STRATIGY_ID, "缺少需要操作的API列表");
        }

        BreakerStrategy breakerStrategy = breakerService.findBreakerStrategy(id);

        if(breakerStrategy==null){
            throw new MsbException(ERROR_STRATEGY_NOT_EXIST,"熔断策略不存在");
        }


        breakerService.bindBreakerStrategyApiBatch(breakerStrategy,param.getApiIdList());

        log(breakerStrategy,StringUtils.join(param.getApiIdList(),","),8);
        return Result.success();
    }

    @PostMapping("/apiStrategy/{id}/serviceApi/delete/batch")
    @ApiOperation(value = "批量删除API绑定熔断策略", notes = "批量删除API绑定熔断策略")
    public Result unbindApiStrategy(@PathVariable("id") @ApiParam("熔断策略主键") Integer id, @RequestBody @ApiParam BindBreakerApiBatchParam param) throws MsbException{
        if(param.getApiIdList().size()==0){
            throw new MsbException(ERROR_MISSING_STRATIGY_ID, "缺少需操作的API列表");
        }

        breakerService.deleteBreakerStrategyApiBatch(param.getApiIdList());
        BreakerStrategy breakerStrategy = breakerService.findBreakerStrategy(id);
        log(breakerStrategy,StringUtils.join(param.getApiIdList(),","),9);
        return Result.success();
    }

    @GetMapping("/break/log")
    @ApiOperation("获取熔断策略操作列表")
    public Response<Page<BreakerStrategyLog>> getList(
            @RequestParam(required = false) Long beginDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(required = false) String strategyName,
            @RequestParam(required = false) String apis,
            @PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BreakerStrategyLog> page =
                this.breakerStrategyLogService.findAll(beginDate,endDate,strategyName,apis,pageable);
        return Response.ok(page);
    }

}
