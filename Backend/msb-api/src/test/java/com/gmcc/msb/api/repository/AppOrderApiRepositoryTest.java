package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.*;
import com.gmcc.msb.common.entity.Operator;
import com.gmcc.msb.common.property.UserContextHolder;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppOrderApiRepositoryTest {
    @Autowired
    private AppOrderApiRepository appOrderApiRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCountByAppId(){
        AppOrderApi apApi = new AppOrderApi();
        apApi.setAppId(1);
        apApi.setApiId(11);

        appOrderApiRepository.save(apApi);

        long result = appOrderApiRepository.countByAppId(1);

        Assert.assertEquals(1,result);
    }
    @Test
    public void testGetAll() throws Exception{
        UserContextHolder.getContext().setDataOrgList(Lists.newArrayList(1L));
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        String serviceId = "service-id";
        Serv serv = getServ(serviceId);

        String appName = "appName";
        App a = new App();
        a.setAppName(appName);
        Operator user = new Operator();
        user.setOrgId(1l);
        a.setUser(user);
        int appId = entityManager.persist(a).getId();


        API api = getApi(serviceId, "/test/", "api-name1", "POST");
        int apiId1 = entityManager.persist(api).getId();

        API api2 = getApi(serviceId, "/test/2", "api-name2", "GET");
        int apiId2 = entityManager.persist(api2).getId();

        AppOrderApi apApi = getAppOrderApi(df, appId, apiId1, "20180903");
        appOrderApiRepository.save(apApi);

        AppOrderApi apApi2 = getAppOrderApi(df, appId, apiId2, "20180904");
        appOrderApiRepository.save(apApi2);

        List<Long> orgIds = new ArrayList<>();
        orgIds.add(1l);
        List<Map<String,Object>> result = appOrderApiRepository.getAll(orgIds);

        Assert.assertNotNull(result);
        Assert.assertEquals(2,result.size());

        for(int i=0;i<2;i++) {
            Map<String,Object> detail = result.get(i);
            Assert.assertEquals(serv.getServiceName(),detail.get("serviceName"));
            Assert.assertEquals(api.getServiceId(),detail.get("serviceId"));
            Assert.assertEquals(a.getAppName(),detail.get(appName));
            if(i==0) {
                Assert.assertEquals(api2.getPath(), detail.get("path"));
                Assert.assertEquals(api2.getMethod(), detail.get("method"));
                Assert.assertEquals(api2.getApiName(), detail.get("apiName"));
            }
            else{
                Assert.assertEquals(api.getPath(), detail.get("path"));
                Assert.assertEquals(api.getMethod(), detail.get("method"));
                Assert.assertEquals(api.getApiName(), detail.get("apiName"));
            }

        }

    }

    private AppOrderApiAudit newAuditByOrder(AppOrderApi apApi) {
        AppOrderApiAudit audit = new AppOrderApiAudit();
        audit.setOrderId(apApi.getId());
        audit.setAppId(apApi.getAppId());
        audit.setApiId(apApi.getApiId());
        audit.setUpdateDate(apApi.getUpdateDate());
        return audit;
    }

    private AppOrderApi getAppOrderApi(DateFormat df, int appId, int apiId1, String s) throws ParseException {
        AppOrderApi apApi = new AppOrderApi();
        apApi.setAppId(appId);
        apApi.setApiId(apiId1);
        apApi.setUpdateDate(df.parse(s));
        return apApi;
    }

    private API getApi(String serviceId, String s, String s2, String post) {
        API api = new API();
        api.setPath(s);
        api.setServiceId(serviceId);
        api.setApiName(s2);
        api.setMethod(post);
        api.setStatus(0);
        return api;
    }

    private Serv getServ(String serviceId) {
        Serv serv = new Serv();
        serv.setServiceName("service-name");
        serv.setServiceId(serviceId);

        entityManager.persist(serv);
        return serv;
    }

    @Test
    public void test_getAuditAll(){
        UserContextHolder.getContext().setDataOrgList(Lists.newArrayList(1L));
        getServ("service-id");

        App a = new App();
        a.setAppName("appName");
        Operator user = new Operator();
        user.setOrgId(1l);
        a.setUser(user);
        int appId = entityManager.persist(a).getId();

        API api = new API();
        api.setPath("/test/");
        api.setServiceId("service-id");
        api.setApiName("api-name1");
        api.setMethod("POST");
        api.setStatus(0);
        int apiId1 = entityManager.persist(api).getId();

        AppOrderApi apApi = new AppOrderApi();
        apApi.setAppId(appId);
        apApi.setApiId(apiId1);
        apApi.setUpdateDate(new Date());
        //appOrderApiRepository.save(apApi);
        entityManager.persist(apApi);

        AppOrderApiAudit audit = new AppOrderApiAudit();
        audit.setOrderId(apApi.getId());
        audit.setAppId(appId);
        audit.setApiId(apiId1);
        audit.setUpdateDate(new Date());
        entityManager.persist(audit);

        List<Map<String, Object>> all = appOrderApiRepository.getAuditAll(Lists.newArrayList(1L));

        assertNotNull(all );
        assertTrue(all.size()>0);
    }

    @Test
    public void test_findAllByAppIdAndTypeId(){
        int appId = 1;
        int typeId = 11;
        AppOrderApi apApi = new AppOrderApi();
        apApi.setAppId(appId);
        apApi.setApiId(typeId);

        appOrderApiRepository.save(apApi);
        List<AppOrderApi> allByAppIdAndTypeId = appOrderApiRepository.findAllByAppIdAndApiId(appId, typeId);
        assertNotNull(allByAppIdAndTypeId );
        assertTrue(allByAppIdAndTypeId.size()>0);
    }
}
