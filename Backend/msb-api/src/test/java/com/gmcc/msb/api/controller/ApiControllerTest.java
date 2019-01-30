package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.ApiAudit;
import com.gmcc.msb.api.service.ApiService;
import com.gmcc.msb.api.service.SyncRedisService;
import com.gmcc.msb.api.vo.request.ApiRequest;
import com.gmcc.msb.api.vo.request.AuditRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.vo.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ApiControllerTest {


    @InjectMocks
    ApiController apiController;

    @Mock
    private ApiService apiService;

    @Mock
    private SyncRedisService syncRedisService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAdd() {

        API api = new API();
        when(apiService.add(any()))
                .thenReturn(api);

        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setApiName("name");
        apiRequest.setMethod("GET");
        apiRequest.setServiceId("msb-api");
        apiRequest.setPath("/abc/def/");

        Response<API> response = apiController.add(apiRequest);

        assertNotNull(response);
        assertEquals(api, response.getContent());
    }

    @Test
    public void testUpdate() {

        API api = new API();
        when(apiService.update(any(), any()))
                .thenReturn(api);

        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setApiName("name");
        apiRequest.setMethod("GET");
        apiRequest.setServiceId("msb-api");
        apiRequest.setPath("/abc/def/");

        Response<API> response = apiController.update(api.getId(), apiRequest);

        assertNotNull(response);
        assertEquals(api, response.getContent());
    }


    @Test
    public void testDelete() {
        assertNotNull(apiController.delete(1));
    }

    @Test(expected = MsbException.class)
    public void testGetNoRecord() {

        when(apiService.findOne(1))
                .thenReturn(null);

        apiController.get(1);
    }

    @Test
    public void testGetById() {

        API api = new API();
        when(apiService.findOne(1))
                .thenReturn(api);
        when(apiService.setServiceName(api))
                .thenReturn(api);

        assertEquals(api, apiController.get(1).getContent());


    }

    @Test
    public void testGet() {

        Pageable pageable = new PageRequest(1, 2);

        List<API> content = Arrays.asList(new API());
        when(apiService.find(null, null, null, null, pageable))
                .thenReturn(new PageImpl<API>(content));

        Response<Page<API>> page = apiController.get(null, null, null, null, pageable);

        assertNotNull(page);
        assertNotNull(page.getContent());
        assertEquals(1, page.getContent().getTotalElements());


    }

    @Test
    public void testApply() {
        API api = new API();

        when(apiService.apply(1, 1))
                .thenReturn(api);

        assertNotNull(apiController.apply(1, 1));
    }

    @Test
    public void testAudit() {
        API api = new API();

        AuditRequest vo = new AuditRequest();
        when(apiService.audit(1, vo))
                .thenReturn(api);

        assertNotNull(apiController.audit(1, vo));
    }


    @Test
    public void testGetAuditList() {
        API api = new API();

        AuditRequest vo = new AuditRequest();
        Pageable pageable = new PageRequest(1, 2);
        List<ApiAudit> content = Arrays.asList(new ApiAudit());
        when(apiService.getAuditList(pageable))
                .thenReturn(new PageImpl<>(content));

        assertNotNull(apiController.getAuditList(pageable));
        assertEquals(1, apiController.getAuditList(pageable).getContent().getTotalElements());
    }

    @Test
    public void testApiUsedByApp() {
        API api = new API();

        AuditRequest vo = new AuditRequest();
        Pageable pageable = new PageRequest(1, 2);
        List<Map<String, Object>> content = Arrays.asList(new HashMap<>());
        when(apiService.appsThatOrderApiId(1, pageable))
                .thenReturn(new PageImpl<>(content));

        assertNotNull(apiController.apiUsedByApp(1, pageable));
        assertEquals(1, apiController.apiUsedByApp(1, pageable).getContent().getTotalElements());
    }
}
