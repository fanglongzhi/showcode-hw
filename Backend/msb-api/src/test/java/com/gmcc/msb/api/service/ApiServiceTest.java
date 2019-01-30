package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.ApiAudit;
import com.gmcc.msb.api.entity.ApiGroupItem;
import com.gmcc.msb.api.entity.Serv;
import com.gmcc.msb.api.repository.ApiGroupItemRepository;
import com.gmcc.msb.api.repository.ApiRepository;
import com.gmcc.msb.api.vo.request.AuditRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ApiServiceTest {

    @InjectMocks
    ApiService apiService;

    @Mock
    ApiRepository apiRepository;

    @Mock
    ServService servService;

    @Mock
    RedisService redisService;

    @Mock
    AppOrderApiService appOrderApiService;

    @Mock
    ApiGroupItemService apiGroupItemService;

    @Mock
    ApiAuditService apiAuditService;

    @Mock
    ApiGroupItemRepository apiGroupItemRepository;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test()
    public void testAddCheckMethod() {

        API api = new API();
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");

        try {
            apiService.add(api);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("0001-10111"));
        }


        api.setMethod("");
        try {
            apiService.add(api);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("0001-10111"));
        }

        api.setMethod("ABC");
        try {
            apiService.add(api);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("0001-10111"));
        }

        api.setMethod("gE T");
        try {
            apiService.add(api);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("0001-10111"));
        }


    }

    @Test
    public void testAddCheckPathFail() {
        API api = new API();
        api.setStatus(1);
        api.setMethod("GET");
        api.setApiName("name");

        pathFailedTest(null, api);
        pathFailedTest("abd", api);
        pathFailedTest("/abd", api);
        pathFailedTest("/abd_", api);
        pathFailedTest("/abd-", api);
        pathFailedTest("/ab#d_", api);
        pathFailedTest("/123$/23", api);
        pathFailedTest("/abd_sd", api);
    }

    private void pathFailedTest(String path, API api) {
        api.setPath(path);
        try {
            apiService.add(api);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("0001-10112"));
        }

    }


    @Test(expected = MsbException.class)
    public void testAddServiceNotExist() {

        API api = new API();
        api.setServiceId("service-a");
        api.setStatus(1);
        api.setMethod("GET");
        api.setApiName("name");
        api.setPath("/name/");

        when(servService.findOne("service-a"))
                .thenReturn(null);

        apiService.add(api);

    }


    @Test
    public void testAdd() {

        API api = new API();
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(4);
        api.setId(1);

        when(apiRepository.save(api)).thenReturn(api);
        when(servService.findOne(api.getServiceId())).thenReturn(new Serv());

        apiService.add(api);

        assertEquals(new Integer(0), api.getStatus());

    }

    @Test
    public void testAddWithPath() {

        API api = new API();
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/def/");
        api.setMethod("GET");
        api.setStatus(4);
        api.setId(1);

        when(apiRepository.save(api)).thenReturn(api);
        when(servService.findOne(api.getServiceId())).thenReturn(new Serv());

        API result = apiService.add(api);
        assertEquals(new Integer(0), result.getStatus());


        api.setPath("/abdc/{id}/def/");
        result = apiService.add(api);
        assertEquals(new Integer(0), result.getStatus());


        api.setPath("/abd--c/_{id}/def/");
        result = apiService.add(api);
        assertEquals(new Integer(0), result.getStatus());


    }

    @Test
    public void testUpdateRecordNotFound() {
        when(apiRepository.findOne(1))
                .thenReturn(null);
        try {
            apiService.update(1, new API());
        } catch (MsbException e) {
            assertThat(e.getMessage(), containsString("0001-10114"));
        }
    }

    @Test
    public void testUpdateWithStatusCheck() {
        API api = new API();
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(1);

        when(apiRepository.findOne(1)).thenReturn(api);


//        updateStatusCheck(API.STATUS_NEW, api);
        updateStatusCheck(API.STATUS_APPLY_ONLINE, api);
        updateStatusCheck(API.STATUS_ONLINE, api);
//        updateStatusCheck(API.STATUS_APPLY_ONLINE_FAIL, api);
        updateStatusCheck(API.STATUS_APPLY_OFFLINE, api);
//        updateStatusCheck(API.STATUS_OFFLINE, api);
        updateStatusCheck(API.STATUS_APPLY_OFFLINE_FAIL, api);
        updateStatusCheck(7, api);


    }

    private void updateStatusCheck(int status, API api) {
        api.setStatus(status);
        try {
            apiService.update(1, api);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("0001-10115"));
        }
    }


    @Test
    public void testUpdate() {

        API api = new API();
        api.setId(1);
        api.setStatus(1);
        api.setServiceId("service-a");
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(0);


        API modify = new API();
        BeanUtils.copyProperties(api, modify);
        modify.setServiceName("others");
        modify.setPath("/abdD/{id}/-/_/kh/");
        modify.setServiceId("service-b");


        when(apiRepository.findOne(1)).thenReturn(api);
        when(apiRepository.save(Matchers.any(API.class)))
                .thenReturn(modify);

        API result = apiService.update(1, modify);

        assertEquals(modify.getApiName(), result.getApiName());
        assertEquals(modify.getPath(), result.getPath());

    }


    @Test
    public void testDeleteRecordNotFound() {
        when(apiRepository.findOne(1))
                .thenReturn(null);
        try {
            apiService.delete(1);
        } catch (MsbException e) {
            assertThat(e.getMessage(), containsString("0001-10116"));
        }
    }


    @Test
    public void testDeleteCheckStatus() {
        API api = new API();
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(4);

        when(apiRepository.findOne(1)).thenReturn(api);

//        deleteStatusCheck(API.STATUS_NEW, api);
        deleteStatusCheck(API.STATUS_APPLY_ONLINE, api);
        deleteStatusCheck(API.STATUS_ONLINE, api);
//        deleteStatusCheck(API.STATUS_APPLY_ONLINE_FAIL, api);
        deleteStatusCheck(API.STATUS_APPLY_OFFLINE, api);
//        deleteStatusCheck(API.STATUS_OFFLINE, api);
        deleteStatusCheck(API.STATUS_APPLY_OFFLINE_FAIL, api);


    }

    private void deleteStatusCheck(int status, API api) {
        api.setStatus(status);
        try {
            apiService.delete(1);
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("0001-10117"));
        }
    }

    @Test
    public void testDelete() {

        API api = new API();
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(0);

        when(apiRepository.findOne(1)).thenReturn(api);

        apiService.delete(1);

        api.setStatus(5);
        apiService.delete(1);

    }


    @Test
    public void testFind() {
        Pageable pageable = new PageRequest(1, 2);
        when(apiRepository.findAllByConditions(
                "serviceName",
                "serviceId",
                "apiname", 0, Lists.newArrayList(),pageable))
                .thenReturn(null);
        UserContextHolder.getContext().setDataOrgList(Lists.newArrayList());
        Page<API> result = apiService.find("serviceName", "serviceId", "apiname", 0, pageable);
        assertNotNull(result);
        assertEquals(0,result.getTotalElements());
    }

    @Test
    public void testFindEmptyParams() {
        Pageable pageable = new PageRequest(1, 2);
        when(apiRepository.findAllByConditions(
                null,
                null, null, null,Lists.newArrayList(), pageable))
                .thenReturn(null);
        UserContextHolder.getContext().setDataOrgList(Lists.newArrayList());
        Page<API> result = apiService.find(null, null, null, null, pageable);
        assertNotNull(result);
        assertEquals(0,result.getTotalElements());
    }

    @Test
    public void testApplyCheck() {
        API api = new API();
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(1);

        when(apiRepository.findOne(1)).thenReturn(api);


        String msg1 = "0001-10118";
//        applyStatusCheck(API.STATUS_NEW, api, 0, msg1);
        applyStatusCheck(API.STATUS_APPLY_ONLINE, api, 0, msg1);
        applyStatusCheck(API.STATUS_ONLINE, api, 0, msg1);
//        applyStatusCheck(API.STATUS_APPLY_ONLINE_FAIL, api, 0, msg1);
        applyStatusCheck(API.STATUS_APPLY_OFFLINE, api, 0, msg1);
//        applyStatusCheck(API.STATUS_OFFLINE, api, 0, msg1);
        applyStatusCheck(API.STATUS_APPLY_OFFLINE_FAIL, api, 0, msg1);


        String msg2 = "0001-10119";
        applyStatusCheck(API.STATUS_NEW, api, 1, msg2);
        applyStatusCheck(API.STATUS_APPLY_ONLINE, api, 1, msg2);
//        applyStatusCheck(API.STATUS_ONLINE, api, 1, msg2);
        applyStatusCheck(API.STATUS_APPLY_ONLINE_FAIL, api, 1, msg2);
        applyStatusCheck(API.STATUS_APPLY_OFFLINE, api, 1, msg2);
        applyStatusCheck(API.STATUS_OFFLINE, api, 1, msg2);
//        applyStatusCheck(API.STATUS_APPLY_OFFLINE_FAIL, api, 1, msg2);


    }

    private void applyStatusCheck(int status, API api, int type, String msg) {
        api.setStatus(status);
        try {
            apiService.apply(1, type); //下线
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(msg));
        }
    }


    @Test(expected = MsbException.class)
    public void testApplyInvalidType() {
        when(apiRepository.findOne(1)).thenReturn(new API());

        apiService.apply(1, 3);
    }

    @Test
    public void testApplyOnline() {
        API api = new API();
        api.setId(1);
        api.setServiceId("service-a");
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(0);

        when(apiRepository.findOne(1)).thenReturn(api);
        when(apiRepository.save(api))
                .thenReturn(api);

        API result = apiService.apply(api.getId(), 0);

        assertEquals(1, result.getStatus().intValue());


        api.setStatus(3);
        result = apiService.apply(api.getId(), 0);
        assertEquals(1, result.getStatus().intValue());
    }


    @Test
    public void testApplyOffline() {
        API api = new API();
        api.setId(1);
        api.setServiceId("service-a");
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(2);

        when(apiRepository.findOne(1)).thenReturn(api);
        when(apiRepository.save(api)).thenReturn(api);
        when(appOrderApiService.isApiOrderedByApp(api.getId())).thenReturn(false);
        when(apiGroupItemService.isApiInGroup(api.getId())).thenReturn(false);

        API result = apiService.apply(api.getId().intValue(), 1);

        assertEquals(4, result.getStatus().intValue());

        api.setStatus(6);
        result = apiService.apply(api.getId().intValue(), 1);

        assertEquals(4, result.getStatus().intValue());

    }

    @Test
    public void testApiCanOffline() {

        API api = new API();
        api.setId(1);
        api.setServiceId("service-a");
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(2);

        when(apiRepository.findOne(1)).thenReturn(api);
        when(apiRepository.save(api)).thenReturn(api);


        when(appOrderApiService.isApiOrderedByApp(api.getId())).thenReturn(true);
//        when(apiGroupItemService.isApiInGroup(api.getId())).thenReturn(false);
        try {
            apiService.apply(api.getId(), 1);
            fail();
        } catch (MsbException e) {
            assertThat(e.getMessage(), containsString("0001-10121"));
        }

//        when(appOrderApiService.isApiOrderedByApp(api.getId())).thenReturn(false);
////        when(apiGroupItemService.isApiInGroup(api.getId())).thenReturn(true);
//        try {
//            apiService.apply(api.getId(), 1);
//            fail();
//        } catch (MsbException e) {
//            assertThat(e.getMessage(), containsString("0001-10122"));
//        }
    }


    @Test(expected = MsbException.class)
    public void testAuditNoRecord() {
        when(apiRepository.findOne(1)).thenReturn(null);

        ApiAudit apiAudit = new ApiAudit();
        when(apiAuditService.findOne(1)).thenReturn(apiAudit);
        when(apiAuditService.save(Matchers.any(ApiAudit.class))).thenReturn(null);

        apiService.audit(1, new AuditRequest());
    }

    @Test
    public void testAuditOnlineCheckStatus() {
        API api = new API();
        api.setId(1);
        api.setServiceId("service-a");
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(2);

        when(apiRepository.findOne(1)).thenReturn(api);
        ApiAudit apiAudit = new ApiAudit();
        apiAudit.setApiId(api.getId());
        when(apiAuditService.findOne(1)).thenReturn(apiAudit);
        when(apiAuditService.save(Matchers.any(ApiAudit.class))).thenReturn(null);


        AuditRequest request = new AuditRequest();
        request.setType(0);
        request.setResult(0);

        try {
            apiService.audit(api.getId(), request);
            fail();
        } catch (MsbException e) {
            assertThat(e.getMessage(), containsString("0001-10124"));
        }
    }

    @Test
    public void testAuditOnline() {
        API api = new API();
        api.setId(1);
        api.setServiceId("service-a");
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(1);

        when(apiRepository.findOne(1)).thenReturn(api);
        when(apiRepository.save(api)).thenReturn(api);

        ApiAudit apiAudit = new ApiAudit();
        apiAudit.setApiId(api.getId());
        when(apiAuditService.findOne(1)).thenReturn(apiAudit);
        when(apiAuditService.save(Matchers.any(ApiAudit.class))).thenReturn(null);


        AuditRequest request = new AuditRequest();
        request.setType(0);
        request.setResult(0);
        API result = apiService.audit(1, request);
        assertEquals(2, result.getStatus().intValue());

        // 审核不通过
        api.setStatus(1);
        request.setResult(1);
        result = apiService.audit(1, request);
        assertEquals(3, result.getStatus().intValue());

    }


    @Test
    public void testAuditOfflineCheckStatus() {
        API api = new API();
        api.setId(1);
        api.setServiceId("service-a");
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(2);

        when(apiRepository.findOne(1)).thenReturn(api);

        ApiAudit apiAudit = new ApiAudit();
        apiAudit.setApiId(api.getId());
        when(apiAuditService.findOne(1)).thenReturn(apiAudit);
        when(apiAuditService.save(Matchers.any(ApiAudit.class))).thenReturn(null);


        AuditRequest request = new AuditRequest();
        request.setType(1);
        request.setResult(0);
        try {
            apiService.audit(api.getId(), request);
            fail();
        } catch (MsbException e) {
            assertThat(e.getMessage(), containsString("0001-10125"));
        }
    }

    @Test
    public void testAuditOffline() {
        API api = new API();
        api.setId(1);
        api.setServiceId("service-a");
        api.setStatus(1);
        api.setApiName("name");
        api.setPath("/abdc/");
        api.setMethod("GET");
        api.setStatus(4);


        when(apiRepository.findOne(1)).thenReturn(api);
        when(apiRepository.save(api)).thenReturn(api);

        ApiAudit apiAudit = new ApiAudit();
        apiAudit.setApiId(api.getId());
        when(apiAuditService.findOne(1)).thenReturn(apiAudit);
        when(apiAuditService.save(Matchers.any(ApiAudit.class))).thenReturn(null);

        AuditRequest request = new AuditRequest();
        request.setType(1);
        request.setResult(0);
        API result = apiService.audit(1, request);
        assertEquals(5, result.getStatus().intValue());


        // 审核不通过
        api.setStatus(4);
        request.setResult(1);
        result = apiService.audit(1, request);
        assertEquals(6, result.getStatus().intValue());
    }


    @Test
    public void testsetServiceName() {

        assertNull(apiService.setServiceName(null));

        API api = new API();
        api.setId(1);
        api.setServiceId("service-a");

        when(servService.findOne(api.getServiceId())).thenReturn(null);
        assertNull(apiService.setServiceName(api).getApiName());


        Serv serv = new Serv();
        serv.setServiceName("name");
        when(servService.findOne(api.getServiceId())).thenReturn(serv);
        API result = apiService.setServiceName(api);
        assertEquals(serv.getServiceName(), result.getServiceName());

    }

    @Test
    public void testAuditList() {

        Pageable pageable = new PageRequest(1, 2);
        when(this.apiRepository.findAllByStatusIn(Matchers.anyList(), any(Pageable.class)))
                .thenReturn(null);
        assertNull(this.apiService.getAuditList(pageable));

    }

    @Test
    public void testAddApi_duplicate() {
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage("0001-10113");
        API api = new API();
        api.setMethod("GET");
        api.setPath("/a/b/");
        List<API> find = new ArrayList<>();
        API apiB = new API();
        apiB.setId(1);
        find.add(apiB);
        when(apiRepository.findByServiceIdAndPathAndMethod(any(), any(), any())).thenReturn(find);
        apiService.add(api);
    }
}
