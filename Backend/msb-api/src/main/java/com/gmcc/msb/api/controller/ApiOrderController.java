package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppOrderApi;
import com.gmcc.msb.api.entity.AppOrderApiAudit;
import com.gmcc.msb.api.service.*;
import com.gmcc.msb.api.vo.request.AppOrderApiRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.util.BeanUtil;
import com.gmcc.msb.common.vo.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.gmcc.msb.api.entity.App.STATUS_AVAILABLE;

@RestController
@RequestMapping("/api/order")
public class ApiOrderController {
    private static final Logger logger = LoggerFactory.getLogger(ApiOrderController.class);
    private static final String ID_IS_NULL = "0001-00002";
    private static final String ORDER_RECORD_EXITS = "0001-10009";
    private static final String STATUS_IS_SUBSCRIBE = "0001-10010";
    private static final String ORDER_RECORD_NOT_EXITS = "0001-10011";
    private static final String STATUS_ISNOT_REPLY = "0001-10012";
    private static final String API_NOT_EXITS = "0001-10013";
    private static final String APP_ISNOT_EXITS = "0001-10014";
    public static final String ERROR_STATUS_UN_AVAILABLE = "0001-10019";
    public static final String STATUS_NU_NOT_APPLY_OR_EXISTS_UN_SUB = "0001-10016";
    public static final String ERROR_APPROVE_DATA_NOT_EXISTS = "0001-10017";
    public static final String ERROR_RESULT_TYPE = "0001-10018";
    public static final String API_STATUS_NOT_TWO = "0001-10142";

    @Autowired
    private AppOrderApiService service;

    @Autowired
    private AppService appService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private AppOrderApiAuditService auditService;

    @Autowired
    private RedisService redisService;

    /**
     * Get All Records
     *
     * @return Response
     */
    @ApiOperation(value = "获取全部订阅")
    @GetMapping
    public Response get(@RequestParam(name="appId",required = false)Integer appId) {
        logger.info("enter {}() method!", "get");
        List<Map<String, Object>> all = service.getAll(appId);
        return Response.ok(all);
    }

    @ApiOperation(value = "获取全部订阅审核")
    @GetMapping("/audit")
    public Response getAudit() {
        logger.info("enter {}() method!", "getAudit");
        List<AppOrderApiAudit> all = service.getAuditAll();
        return Response.ok(all);
    }


    /**
     * Get one record by ID
     *
     * @param id
     * @return Response
     */
    @ApiOperation(value = "根据订阅ID获得订阅消息")
    @GetMapping("/{id}")
    public Response getOne(@PathVariable("id") Integer id) {
        logger.info("enter {}({}) method!", "getOne", id);
        AppOrderApi one = service.findOne(id);
        return Response.ok(one);
    }


    /**
     * Add new record
     *
     * @param entity
     * @return Response
     */
    @ApiOperation(value = "新增订阅")
    @PostMapping
    @Transactional
    public Response save(@RequestBody @Valid @ApiParam(name = "订阅对象", value = "type取值范围0:API, 1:API group；status取值范围0:订阅申请中、1:订阅申请不通过、2:已订阅、3:已退订", required = true) AppOrderApiRequest requestEntity) {
        if (requestEntity.getStartDate() == null) {
            throw new MsbException("0001-00023");
        }
        if (requestEntity.getEndDate() == null) {
            throw new MsbException("0001-00024");
        }

        if (requestEntity.getEndDate().before(requestEntity.getStartDate())) {
            throw new MsbException("0001-00025");
        }

        checkApp(requestEntity.getAppId());
        checkAPI(requestEntity.getApiId());

        AppOrderApi entity = new AppOrderApi();
        BeanUtils.copyProperties(requestEntity, entity);

        entity.setStatus(0);
        Date date = new Date();
        entity.setCreateDate(date);
        entity.setUpdateDate(date);

        service.setStartDate(entity);
        service.setEndDate(entity);

        //检查时间重叠
        List<AppOrderApi> list = service.checkTimeOverlap(entity);
        if (list != null && !list.isEmpty()) {
            throw new MsbException("0001-10136");
        }
        entity = service.save(entity);
        newAudit(entity, date);
        return Response.ok();
    }

    private void newAudit(AppOrderApi entity, Date date) {
        AppOrderApiAudit apiAudit = newAudit(entity);
        apiAudit.setApplyDate(date);
        apiAudit.setUpdateDate(date);
        auditService.save(apiAudit);
    }

    public AppOrderApiAudit newAudit(AppOrderApi entity) {
        AppOrderApiAudit audit = new AppOrderApiAudit();
        audit.setOrderId(entity.getId());
        audit.setAppId(entity.getAppId());
        audit.setApiId(entity.getApiId());
        audit.setStartDate(entity.getStartDate());
        audit.setEndDate(entity.getEndDate());
        audit.setApplyType(0);
        return audit;
    }

    private void checkApp(int appId) {
        App app = appService.findOneApp(appId);
        if (app == null) {
            throw new MsbException(APP_ISNOT_EXITS);
        }
        if (app.getStatus() == null || STATUS_AVAILABLE != app.getStatus()) {
            throw new MsbException(ERROR_STATUS_UN_AVAILABLE);
        }
    }

    private void checkAPI(int apiId) {
        API api = apiService.findOne(apiId);
        if (api == null) {
            throw new MsbException(API_NOT_EXITS);
        }

        if(api.getStatus() != 2){
            throw new MsbException(API_STATUS_NOT_TWO);
        }
    }

    /**
     * Update one record
     *
     * @param entity
     * @return Response
     */
    @ApiOperation(value = "修改订阅,只修改生失效时间，只有审批失败或者新增的才能修改")
    @PutMapping
    public Response update(@RequestBody @ApiParam(name = "订阅对象", value = "type取值范围0:API, 1:API group；status取值范围0:订阅申请中、1:订阅申请不通过、2:已订阅、3:已退订", required = true) AppOrderApiRequest entity) {
        Assert.notNull(entity, "0001-00026");
        Assert.notNull(entity.getId(), "0001-00027");
        AppOrderApi findOne = service.findOne(entity.getId());
        if (findOne == null) {
            throw new MsbException("0001-10137");
        }

        if (AppOrderApi.APPLY_FAIL != findOne.getStatus()) {
            throw new MsbException("0001-10138");
        }

        BeanUtil.copyNonNullProperties(entity, findOne);

        service.setStartDate(findOne);
        service.setEndDate(findOne);

        //检查时间重叠
        List<AppOrderApi> list = service.checkTimeOverlap(findOne);
        if (list != null && !list.isEmpty()) {
            for (AppOrderApi appOrderApi : list) {
                if (!appOrderApi.getId().equals(findOne.getId())) {
                    throw new MsbException("0001-10139");
                }
            }
        }

        entity.setUpdateDate(new Date());
        service.save(findOne);
        return Response.ok();
    }

    /**
     * Delete record
     *
     * @param id
     * @return Response
     */
    @ApiOperation(value = "根据ID删除订阅")
    @DeleteMapping("/{id}")
    public Response delete(@PathVariable Integer id) {
        logger.info("enter into {}({}) method!", "delete", id);
        if (id == null) {
            throw new MsbException(ID_IS_NULL);
        }
        AppOrderApi one = service.findOne(id);
        if(one == null){
            throw new MsbException("0001-10137");
        }

        if(one.getStatus() != 1 && one.getStatus() != 3){
            throw new MsbException("0001-10138");
        }
        service.delete(id);

        redisService.deleteAppOrderApi(one);

        return Response.ok();
    }

    /**
     * Delete record list
     *
     * @param ids
     * @return Response
     */
    @ApiOperation(value = "根据ID删除订阅")
    @PostMapping("/delete/list")
    public Response deleteList(@RequestBody Integer[] ids) {
        logger.info("enter into {}({}) method!", "deleteList", ids);
        List<Integer> list = CollectionUtils.arrayToList(ids);
        List<AppOrderApi> all = service.findAllByIdIn(list);
        service.deleteList(list);

        for (AppOrderApi appOrderApi : all){
            redisService.deleteAppOrderApi(appOrderApi);
        }

        return Response.ok();
    }

    /**
     * Unsubscribe api
     *
     * @param id 根据ID取消订阅
     * @return Response
     */
    @ApiOperation(value = "根据ID取消订阅")
    @PostMapping("/unsubscribe/{id}")
    public Response unsubscribe(@PathVariable Integer id) {
        logger.info("enter into {}({}) method!", "unsubscribe", id);
        AppOrderApi one = service.findOne(id);
        if (one == null) {
            throw new MsbException(ORDER_RECORD_EXITS);
        }
        if (one.getStatus() != 2) {
            throw new MsbException(STATUS_IS_SUBSCRIBE);
        }

        one.setStatus(3);
        service.save(one);

        redisService.addAppOrderApi(one);

        return Response.ok();
    }

    /**
     * Unsubscribe api
     *
     * @param ids 根据ID集合取消订阅
     * @return Response
     */
    @ApiOperation(value = "根据ID集合取消订阅")
    @PostMapping("/unsubscribe")
    public Response batchUnsubscribe(@RequestBody Integer[] ids) {
        logger.info("enter into {}({}) method!", "batchUnsubscribe", ids);
        List<Integer> list = CollectionUtils.arrayToList(ids);
        service.batchUnsubscribe(list);

        List<AppOrderApi> all = service.findAllByIdIn(list);

        for (AppOrderApi appOrderApi : all){
            redisService.addAppOrderApi(appOrderApi);
        }
        return Response.ok();
    }


    @ApiOperation("审批失败后，再次申请订阅")
    @PostMapping("/apply/{id}")
    @Transactional
    public Response apply(@PathVariable("id") Integer id) {
        AppOrderApi one = service.findOne(id);
        if(one == null){
            throw new MsbException("0001-10137");
        }

        if (one.getStatus() != 1 && one.getStatus() != 3) {
            throw new MsbException(STATUS_NU_NOT_APPLY_OR_EXISTS_UN_SUB);
        }
        one.setStatus(0);

        List<AppOrderApi> list = service.checkTimeOverlap(one);
        if (list != null && !list.isEmpty()) {
            for (AppOrderApi appOrderApi : list) {
                if (!appOrderApi.getId().equals(one.getId())) {
                    throw new MsbException("0001-10139");
                }
            }
        }

        service.save(one);
        newAudit(one, new Date());
        return Response.ok();
    }

    /**
     * approve api
     *
     * @param auditId
     * @return Response
     */

    @ApiOperation(value = "1：不通过，2：通过")
    @PostMapping("/approve/{auditId}/{optID}")
    @Transactional
    public Response approve(@PathVariable("auditId") Integer auditId, @PathVariable("optID") @ApiParam(name = "optID", value = "1：不通过，2：通过") Integer optID) {
        logger.info("enter into {}({},{}) method!", "approve", auditId, optID);

        AppOrderApiAudit audit = auditService.findOne(auditId);
        if (audit == null) {
            throw new MsbException(ERROR_APPROVE_DATA_NOT_EXISTS);
        }

        AppOrderApi one = service.findOne(audit.getOrderId());
        if (one == null) {
            throw new MsbException(ORDER_RECORD_NOT_EXITS);
        }

        if (optID == 1) {
            //不通过
            if (one.getStatus() != 0) {
                throw new MsbException(STATUS_ISNOT_REPLY);
            }
            one.setStatus(1);
            audit.setAuditResultFail();
        } else if (optID == 2) {
            //通过
            if (one.getStatus() != 0) {
                throw new MsbException(STATUS_ISNOT_REPLY);
            }
            one.setStatus(2);
            audit.setAuditResultSuccess();
        } else {
            throw new MsbException(ERROR_RESULT_TYPE);
        }
        Date date = new Date();
        one.setUpdateDate(date);
        service.save(one);
        audit.setAuditDate(date);
        audit.setUpdateDate(date);
        auditService.update(audit);

        redisService.addAppOrderApi(one);

        return Response.ok();
    }


}
