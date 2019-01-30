package com.gmcc.msb.msbservice.repository;

import com.gmcc.msb.msbservice.entity.ServiceItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ServiceItemRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Test
    public void findByServiceIdTest() {
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceName("TEST");
        serviceItem.setServiceId("TEST");
        this.testEntityManager.persist(serviceItem);
        int si = serviceItemRepository.countByServiceId("test", "TEST");

        assertEquals(1,si);
    }
    @Test
    public void findByServiceIdAndServiceNameTest_Exist_lower(){
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceId("d1");
        serviceItem.setServiceName("dn1");
        serviceItemRepository.save(serviceItem);

        int si = serviceItemRepository.countByServiceIdAndServiceName("d1","D1","no");
        Assert.assertEquals(1,si);
    }

    @Test
    public void findByServiceIdAndServiceNameTest_Exist_upper(){
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceId("D2");
        serviceItem.setServiceName("dn2");
        serviceItemRepository.save(serviceItem);

        int si = serviceItemRepository.countByServiceIdAndServiceName("d2","D2","no");
        Assert.assertEquals(1,si);
    }
    @Test
    public void findByServiceIdAndServiceNameTest_Exist_serviceName(){
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceId("D3");
        serviceItem.setServiceName("dname");
        serviceItemRepository.save(serviceItem);

        int si = serviceItemRepository.countByServiceIdAndServiceName("t","T","dname");
        Assert.assertEquals(1,si);
    }

    @Test
    public void findByServiceIdAndServiceNameTest_Null(){
        int si = serviceItemRepository.countByServiceIdAndServiceName("no","no","no");
        Assert.assertEquals(0,si);
    }

    @Test
    public void findByServiceCodeEqualsTest() {
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceName("TEST");
        serviceItem.setServiceId("TEST");
        String serviceCode = "9999";
        serviceItem.setServiceCode(serviceCode);
        this.testEntityManager.persist(serviceItem);
        List<ServiceItem> byServiceCodeEquals = serviceItemRepository.findByServiceCodeEquals(serviceCode);

        assertEquals(1,byServiceCodeEquals.size());
    }
}
