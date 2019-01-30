package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppOrderApi;
import com.gmcc.msb.api.entity.Serv;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private AppOrderApiRepository appOrderApiRepository;

    @Test
    public void testAppsOrderedApiId(){
        App app = new App();
        app.setAppName("testAPP");
        int appId = appRepository.save(app).getId();

        AppOrderApi apApi = new AppOrderApi();
        apApi.setAppId(appId);
        apApi.setApiId(11);

        appOrderApiRepository.save(apApi);

        Page<Map<String, Object>> result = appRepository.appsOrderedApiId(11,new PageRequest(0,100));

        Assert.assertEquals(1,result.getTotalElements());
        Assert.assertEquals(app.getAppName(),result.iterator().next().get("appName"));

    }
    @Test
    public void testCountByAppName(){
        App app = new App();
        app.setAppName("testAPP");
        appRepository.save(app).getId();
        long count = appRepository.countByAppName(app.getAppName());
        Assert.assertEquals(1,count);
    }

    @Test
    public void testCountByAppNameAndId(){
        App app = new App();
        app.setAppName("testAPP");
        int id = appRepository.save(app).getId();
        long count = appRepository.countByAppNameAndIdNot(app.getAppName(),id);
        Assert.assertEquals(0,count);
    }
    @Test
    public void testCountByAppNameAndIdFail(){
        App app = new App();
        app.setAppName("testAPP2");
        int id = appRepository.save(app).getId();
        long count = appRepository.countByAppNameAndIdNot(app.getAppName(),id+1);
        Assert.assertEquals(1,count);
    }
    @Test
    public void testFindApiOfAppSubscribe(){
        Serv serv = new Serv();
        serv.setServiceName("service-name");
        serv.setServiceId("service-id");

        entityManager.persist(serv).getId();

        App app = new App();
        app.setAppName("testAPP3");
        int id = appRepository.save(app).getId();

        API api = new API();
        api.setPath("/test/");
        api.setServiceId("service-id");
        api.setApiName("api-name");
        api.setMethod("POST");
        api.setStatus(0);

        int apiId = entityManager.persist(api).getId();

        AppOrderApi ao = new AppOrderApi();
        ao.setApiId(apiId);
        ao.setAppId(id);

        entityManager.persist(ao);

        List<Map<String,Object>> result = appRepository.findApiOfAppSubscribe(id);

        Assert.assertNotNull(result);
        Assert.assertEquals(1,result.size());
        Map<String,Object> detail = result.get(0);

        Assert.assertEquals(serv.getServiceName(),detail.get("serviceName"));
        Assert.assertEquals(api.getServiceId(),detail.get("serviceId"));
        Assert.assertEquals(api.getPath(),detail.get("path"));
        Assert.assertEquals(api.getMethod(),detail.get("method"));
        Assert.assertEquals(api.getApiName(),detail.get("apiName"));
    }
}
