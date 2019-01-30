package com.gmcc.msb.msbsystem.repository;

import com.gmcc.msb.msbsystem.entity.API;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @program: msb-system
 * @description: Api DAO UT
 * @author: zhifanglong
 * @create: 2018-10-11 14:47
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ApiRepositoryTest {
    @Autowired
    private ApiRepository apiRepository;

    @Test
    public void findByIdInTest(){
        API api = new API();
        api.setId(1);
        api.setApiName("testName1");
        api.setPath("/test/1");

        API api2 = new API();
        api2.setId(2);
        api2.setApiName("testName2");
        api2.setPath("/test/2");

        API api3 = new API();
        api3.setId(3);
        api3.setApiName("testName3");
        api3.setPath("/test/3");

        apiRepository.save(api);
        apiRepository.save(api2);
        apiRepository.save(api3);

        List<API> result = apiRepository.findByIdIn(Lists.newArrayList(1,2));

        Assert.assertNotNull(result);
        Assert.assertEquals(2,result.size());
    }
}
