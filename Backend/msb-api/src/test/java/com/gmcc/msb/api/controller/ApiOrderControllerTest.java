package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppOrderApi;
import com.gmcc.msb.api.entity.AppOrderApiAudit;
import com.gmcc.msb.api.service.*;
import com.gmcc.msb.api.vo.request.AppOrderApiRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.vo.Response;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class ApiOrderControllerTest {

    @Mock
    private AppOrderApiService service;

    @Mock
    private AppService appService;

    @Mock
    private ApiService apiService;

    @Mock
    private AppOrderApiAuditService auditService;

    @InjectMocks
    private ApiOrderController controller;

    @Mock
    RedisService redisService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_get() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        when(service.getAll(1)).thenReturn(list);
        Response response = controller.get(1);
        assertEquals("0", response.getCode());
        assertNotNull(response.getContent());
        verify(service).getAll(1);

    }

    @Test
    public void test_getAudit() {
        AppOrderApiAudit audit = new AppOrderApiAudit();
        audit.setId(1);
        ArrayList<AppOrderApiAudit> list = new ArrayList<>();
        list.add(audit);
        when(service.getAuditAll()).thenReturn(list);
        Response response = controller.getAudit();
        assertEquals("0", response.getCode());
        assertNotNull(response.getContent());
        verify(service).getAuditAll();

    }

    @Test
    public void test_getOne() {
        int id = 1;
        AppOrderApi orderApi = getAppOrderApi(id);
        when(service.findOne(anyInt())).thenReturn(orderApi);
        Response response = controller.getOne(id);
        assertEquals("0", response.getCode());
        verify(service).findOne(anyInt());
    }

//    @Test(expected = MsbException.class)
//    public void test_save_exit_order() {
//        int id = 1;
//        AppOrderApi orderApi = getAppOrderApi(id);
//        List<AppOrderApi> list = new ArrayList<>();
//        list.add(orderApi);
//        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(list);
//        controller.save(orderApi);
//    }

    private AppOrderApi getAppOrderApi(int id) {
        AppOrderApi orderApi = new AppOrderApi();
        orderApi.setId(id);
        return orderApi;
    }

    private AppOrderApiRequest getAppOrderApiRequest(AppOrderApi appOrderApi){
        AppOrderApiRequest request = new AppOrderApiRequest();
        BeanUtils.copyProperties(appOrderApi, request);
        return request;
    }

    @Test(expected = MsbException.class)
    public void test_save_without_app() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);

        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(null);
        when(appService.findOneApp(anyInt())).thenReturn(null);
        controller.save(getAppOrderApiRequest(orderApi));
        verify(service).findAppIdAndTypeId(anyInt(), anyInt());
        verify(appService).findOneApp(anyInt());
        verify(apiService, times(0)).findOne(anyInt());
        verify(service, times(0)).save(any());
    }

    private AppOrderApi getAppOrderApi(int id, int appId, int apiId) {
        AppOrderApi orderApi = getAppOrderApi(id);
        orderApi.setAppId(appId);
        orderApi.setApiId(apiId);
        return orderApi;
    }

    @Test(expected = MsbException.class)
    public void test_save_without_api() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);

        App app = new App();
        app.setId(1);
        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(null);
        when(appService.findOneApp(anyInt())).thenReturn(app);
        when(apiService.findOne(anyInt())).thenReturn(null);
        controller.save(getAppOrderApiRequest(orderApi));
        verify(service).findAppIdAndTypeId(anyInt(), anyInt());
        verify(appService).findOneApp(anyInt());
        verify(apiService).findOne(anyInt());
        verify(service, times(0)).save(any());
    }

    @Test
    public void test_save_success() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStartDate(new Date());
        orderApi.setEndDate(DateUtils.addDays(new Date(), 1));
        App app = new App();
        app.setId(appId);
        app.setStatus(App.STATUS_AVAILABLE);
        API api = new API();
        api.setId(apiId);
        api.setStatus(2);
        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(null);
        when(appService.findOneApp(anyInt())).thenReturn(app);
        when(apiService.findOne(anyInt())).thenReturn(api);
        when(service.save(anyObject())).thenReturn(orderApi);
        Response response = controller.save(getAppOrderApiRequest(orderApi));
        assertEquals("0", response.getCode());
//        verify(service).findAppIdAndTypeId(anyInt(), anyInt());
        verify(appService).findOneApp(anyInt());
        verify(apiService).findOne(anyInt());
        verify(service).save(any());
    }

    @Test(expected = MsbException.class)
    public void test_update_exits_order() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);

        List<AppOrderApi> list = new ArrayList<>();
        list.add(orderApi);

        AppOrderApi orderApi1 = getAppOrderApi(id, appId, apiId);
        list.add(orderApi1);

        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(list);
        controller.update(getAppOrderApiRequest(orderApi));
    }


    @Test(expected = MsbException.class)
    public void test_update_diff_order() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);

        List<AppOrderApi> list = new ArrayList<>();

        AppOrderApi orderApi1 = getAppOrderApi(2, appId, apiId);
        list.add(orderApi1);

        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(list);
        controller.update(getAppOrderApiRequest(orderApi));
    }

    @Test(expected = MsbException.class)
    public void test_update_without_app() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);

        App app = new App();
        app.setId(appId);
        API api = new API();
        api.setId(apiId);

        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(null);
        when(appService.findOneApp(anyInt())).thenReturn(app);

        controller.update(getAppOrderApiRequest(orderApi));
        verify(service).findAppIdAndTypeId(anyInt(), anyInt());
        verify(appService).findOneApp(anyInt());
        verify(apiService, times(0)).findOne(anyInt());
        verify(service, times(0)).save(any());
    }

    @Test(expected = MsbException.class)
    public void test_update_without_api() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);

        App app = new App();
        app.setId(appId);
        API api = new API();
        api.setId(apiId);

        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(null);
        when(appService.findOneApp(anyInt())).thenReturn(app);
        when(apiService.findOne(anyInt())).thenReturn(null);
        controller.update(getAppOrderApiRequest(orderApi));
        verify(service).findAppIdAndTypeId(anyInt(), anyInt());
        verify(appService).findOneApp(anyInt());
        verify(apiService, times(1)).findOne(anyInt());
        verify(service, times(0)).save(any());
    }

    @Test()
    public void test_update() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStartDate(new Date());
        orderApi.setEndDate(DateUtils.addDays(new Date(), 1));

        App app = new App();
        app.setId(appId);
        app.setStatus(App.STATUS_AVAILABLE);
        API api = new API();
        api.setId(apiId);
        api.setStatus(2);
        when(service.findAppIdAndTypeId(anyInt(), anyInt())).thenReturn(null);
        when(appService.findOneApp(anyInt())).thenReturn(app);
        when(apiService.findOne(anyInt())).thenReturn(api);
        when(service.save(any())).thenReturn(orderApi);
        Response response = controller.save(getAppOrderApiRequest(orderApi));
        assertEquals("0", response.getCode());
//        verify(service).findAppIdAndTypeId(anyInt(), anyInt());
        verify(appService).findOneApp(anyInt());
        verify(apiService, times(1)).findOne(anyInt());
        verify(service, times(1)).save(any());
    }

    @Test(expected = MsbException.class)
    public void test_delete_without_null() {
        controller.delete(null);
    }

    @Test
    public void test_delete() {
        AppOrderApi orderApi = new AppOrderApi();
        orderApi.setId(1);
        orderApi.setStatus(1);
        when(service.findOne(anyInt())).thenReturn(orderApi);
        Response response = controller.delete(1);
        assertEquals("0", response.getCode());
        verify(service).delete(anyInt());
    }

    @Test(expected = MsbException.class)
    public void test_unsubscribe_exits() {
        when(service.findOne(anyInt())).thenReturn(null);
        controller.unsubscribe(1);
    }

    @Test(expected = MsbException.class)
    public void test_unsubscribe_with_status_neq_2() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(1);
        when(service.findOne(anyInt())).thenReturn(orderApi);
        controller.unsubscribe(1);
    }

    @Test
    public void test_unsubscribe() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(2);
        when(service.findOne(anyInt())).thenReturn(orderApi);
        when(service.save(any())).thenReturn(orderApi);
        Response response = controller.unsubscribe(1);
        assertEquals("0", response.getCode());
        verify(service).findOne(anyInt());
        verify(service).save(any());
    }

    @Test(expected = MsbException.class)
    public void test_approve() {
        when(auditService.findOne(anyInt())).thenReturn(new AppOrderApiAudit());
        when(service.findOne(anyInt())).thenReturn(null);
        controller.approve(1, 1);
    }

    @Test(expected = MsbException.class)
    public void test_approve_with_status_2_opt_0_fail() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(2);
        when(auditService.findOne(anyInt())).thenReturn(new AppOrderApiAudit());
        when(service.findOne(anyInt())).thenReturn(orderApi);
        controller.approve(1, 0);
    }

    @Test(expected = MsbException.class)
    public void test_approve_with_status_0_opt_0_fail() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(0);
        when(auditService.findOne(anyInt())).thenReturn(new AppOrderApiAudit());
        when(service.findOne(anyInt())).thenReturn(orderApi);
        controller.approve(1, 0);
    }


    @Test(expected = MsbException.class)
    public void test_approve_with_status_neq_0_opt_1_fail() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(1);
        when(auditService.findOne(anyInt())).thenReturn(new AppOrderApiAudit());
        when(service.findOne(anyInt())).thenReturn(orderApi);
        controller.approve(1, 1);
    }

    @Test
    public void test_approve_with_status_0_opt_1_without_audit() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(0);
        when(auditService.findOne(anyInt())).thenReturn(new AppOrderApiAudit());
        when(service.findOne(anyInt())).thenReturn(orderApi);
        when(auditService.findByOrderIdAndStatus(orderApi.getId())).thenReturn(null);
        Response response = controller.approve(1, 1);
        assertEquals("0", response.getCode());
        verify(service).findOne(anyInt());
        verify(service).save(any());
    }

    @Test
    public void test_approve_with_status_0_opt_1_success() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(0);
        AppOrderApiAudit audit = controller.newAudit(orderApi);
        ArrayList<AppOrderApiAudit> li = new ArrayList<>();
        li.add(audit);
        when(auditService.findOne(anyInt())).thenReturn(new AppOrderApiAudit());
        when(auditService.findByOrderIdAndStatus(orderApi.getId())).thenReturn(li);
        when(service.findOne(anyInt())).thenReturn(orderApi);
        Response response = controller.approve(1, 1);
        assertEquals("0", response.getCode());
        verify(service).findOne(anyInt());
        verify(service).save(any());
    }

    @Test(expected = MsbException.class)
    public void test_approve_with_status_neq_0_opt_2_fail() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(1);
        when(auditService.findOne(anyInt())).thenReturn(new AppOrderApiAudit());
        when(service.findOne(anyInt())).thenReturn(orderApi);
        controller.approve(1, 2);
    }

    @Test
    public void test_approve_with_status_0_opt_2_success() {
        int id = 1;
        int appId = 11;
        int apiId = 12;
        AppOrderApi orderApi = getAppOrderApi(id, appId, apiId);
        orderApi.setStatus(0);
        AppOrderApiAudit audit = controller.newAudit(orderApi);
        ArrayList<AppOrderApiAudit> li = new ArrayList<>();
        li.add(audit);
        when(auditService.findOne(anyInt())).thenReturn(new AppOrderApiAudit());
        when(auditService.findByOrderIdAndStatus(orderApi.getId())).thenReturn(li);
        when(service.findOne(anyInt())).thenReturn(orderApi);
        Response response = controller.approve(1, 2);
        assertEquals("0", response.getCode());
        verify(service).findOne(anyInt());
        verify(service).save(any());
    }

}
