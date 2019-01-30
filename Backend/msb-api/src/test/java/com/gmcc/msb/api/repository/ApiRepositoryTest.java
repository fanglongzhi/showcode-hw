package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.API;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ApiRepositoryTest {
    @Autowired
    private ApiRepository apiRepository;

    @Test
    public void testCountByServiceIdAndPath(){
        API api = new API();
        api.setServiceId("service-a");
        api.setMethod("GET");
        api.setPath("/a/b/");
        api.setStatus(1);
        api.setServiceName("name");
        api.setApiName("api-name");

        apiRepository.save(api);

        Iterable<API> result = apiRepository.findByServiceIdAndPathAndMethod(api.getServiceId(),
                api.getPath(), api.getMethod());

        assertNotNull(result);

    }
}
