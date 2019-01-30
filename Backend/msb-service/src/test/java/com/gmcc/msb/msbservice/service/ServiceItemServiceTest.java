package com.gmcc.msb.msbservice.service;

import com.gmcc.msb.common.entity.Operator;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.common.service.client.MsbConfigClient;
import com.gmcc.msb.msbservice.entity.ServiceItem;
import com.gmcc.msb.msbservice.repository.ServiceItemRepository;
import com.gmcc.msb.msbservice.vo.ServiceItemVo;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ServiceItemServiceTest {
    private ServiceItemService serviceItemService;
    @Autowired
    private ServiceItemRepository repository;
    @Mock
    private DiscoveryClient discoveryClient;
    @Mock
    private MsbConfigClient msbConfigClient;


    @Before
    public void setUp(){
        //MockitoAnnotations.initMocks(this);
        serviceItemService = new ServiceItemService(discoveryClient,repository,msbConfigClient);

        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceName("a");
        serviceItem.setServiceId("a");
        Operator user1 = new Operator();
        user1.setOrgId(1l);
        serviceItem.setUser(user1);
        repository.save(serviceItem);

        ServiceItem serviceItem2 = new ServiceItem();
        serviceItem2.setServiceName("b");
        serviceItem2.setServiceId("b");
        Operator user2 = new Operator();
        user2.setOrgId(1l);
        serviceItem2.setUser(user2);
        repository.save(serviceItem2);

        ServiceItem serviceItem3 = new ServiceItem();
        serviceItem3.setServiceName("c");
        serviceItem3.setServiceId("c");
        Operator user3 = new Operator();
        user3.setOrgId(1l);
        serviceItem3.setUser(user3);
        repository.save(serviceItem3);
    }

    @Test
    public void syncServiceTest(){

        List<String> serviceList = Lists.newArrayList("b","d");
        when(discoveryClient.getServices()).thenReturn(serviceList);

        List<String> result = serviceItemService.syncServiceList();

        Assert.assertNotNull(result);
        Assert.assertEquals(2,result.size());

        Iterable<ServiceItem> persisData = repository.findAll();

        Assert.assertNotNull(persisData);
        List<ServiceItem> dataList = Lists.newArrayList(persisData);

        Assert.assertEquals(4,dataList.size());

        boolean hasA = false;
        boolean hasB = false;
        boolean hasC = false;
        boolean hasD = false;
        for(ServiceItem item:dataList){
            if(item.getServiceId().equals("a")) hasA=true;
            if(item.getServiceId().equals("b")) hasB=true;
            if(item.getServiceId().equals("c")) hasC=true;
            if(item.getServiceId().equals("d")) hasD=true;
        }

        Assert.assertTrue(hasA&&hasB&&hasC&&hasD);


    }

    @Test
    public void syncServiceTest_null(){


        when(discoveryClient.getServices()).thenReturn(null);

        List<String> result = serviceItemService.syncServiceList();

        Assert.assertNull(result);
    }

    @Test
    public void syncServiceTest_exist(){

        List<String> serviceList = Lists.newArrayList("b","c");
        when(discoveryClient.getServices()).thenReturn(serviceList);

        List<String> result = serviceItemService.syncServiceList();

        Assert.assertNotNull(result);
        Assert.assertEquals(2,result.size());

        Iterable<ServiceItem> persisData = repository.findAll();

        Assert.assertNotNull(persisData);
        List<ServiceItem> dataList = Lists.newArrayList(persisData);

        Assert.assertEquals(3,dataList.size());

        boolean hasA = false;
        boolean hasB = false;
        boolean hasC = false;
        for(ServiceItem item:dataList){
            if(item.getServiceId().equals("a")) hasA=true;
            if(item.getServiceId().equals("b")) hasB=true;
            if(item.getServiceId().equals("c")) hasC=true;
        }

        Assert.assertTrue(hasA&&hasB&&hasC);

    }


    @Test
    public void findServiceItemTest(){
        UserContextHolder.getContext().setDataOrgList(Lists.newArrayList(1l));
        List<ServiceItem> result = serviceItemService.findServiceItem();

        Assert.assertEquals("c",result.get(0).getServiceId());
        Assert.assertEquals("b",result.get(1).getServiceId());
        Assert.assertEquals("a",result.get(2).getServiceId());


    }

   @Test
    public void createServiceItemTest(){
       ServiceItemVo param = new ServiceItemVo();
       param.setServiceId("TEST");
       param.setServiceName("TEST");
       ServiceItem result = serviceItemService.createServiceItem(param);

       Assert.assertNotNull(param);
       Assert.assertEquals("test",result.getServiceId());
       Assert.assertEquals("TEST",result.getServiceName());
   }
   @Test
    public void checkServiceItemDuplicateTest_True(){
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceId("d");
        serviceItem.setServiceName("d");
        repository.save(serviceItem);
        boolean result = serviceItemService.checkServiceItemDuplicate("d","d");
        Assert.assertTrue(result);
   }



    @Test
    public void checkServiceItemDuplicateTest_False(){
        boolean result = serviceItemService.checkServiceItemDuplicate("d","d");
        Assert.assertFalse(result);
    }

    @Test
    public void checkServiceCodeDuplicateTest_True(){
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceId("d");
        serviceItem.setServiceName("d");
        String serviceCode = "9999";
        serviceItem.setServiceCode(serviceCode);
        repository.save(serviceItem);
        boolean result = serviceItemService.checkServiceCodeDuplicate(serviceCode);
        Assert.assertTrue(result);
    }

    @Test
    public void checkServiceCodeDuplicateTest_False(){
        boolean result = serviceItemService.checkServiceCodeDuplicate("9998");
        Assert.assertFalse(result);
    }

}
