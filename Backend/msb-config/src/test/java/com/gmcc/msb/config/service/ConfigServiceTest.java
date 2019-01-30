package com.gmcc.msb.config.service;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.gmcc.msb.config.config.MyProperties;
import com.gmcc.msb.config.entity.Config;
import com.gmcc.msb.config.repository.ConfigRepository;
import com.gmcc.msb.config.vo.ConfigVo;
import com.netflix.appinfo.InstanceInfo;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ConfigServiceTest {
    @InjectMocks
    private ConfigService configService;
    @Mock
    private ConfigRepository configRepository;
    @Mock
    private DiscoveryClient discoveryClient;
    @Mock
    private MyProperties myProperties;
    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(
            WireMockSpring.options().dynamicPort());
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createConfigTest(){
        ConfigVo config = new ConfigVo();
        configService.createConfig(config);

        Assert.assertTrue(true);
    }
    @Test
    public void countConfigTest(){
        ConfigVo configVo = new ConfigVo();
        when(configRepository.countConfig(anyString(),anyString(),anyString(),anyString())).thenReturn(1);
        int count = configService.countConfig(configVo);

        Assert.assertEquals(1,count);
    }

    @Test
    public void deleteConfigTest(){
        when(configRepository.findOne(anyInt())).thenReturn(new Config());
        configService.deleteConfig(1);
        verify(configRepository,times(1)).delete(anyInt());
    }

    @Test
    public void modifyConfigTest(){
        ConfigVo vo = new ConfigVo();
        when(configRepository.findOne(anyInt())).thenReturn(new Config());
        configService.modifyConfig(vo);
        verify(configRepository,times(1)).modifyConfig(anyInt(),anyString(),anyString(),anyString(),anyString());
        Assert.assertEquals("master",vo.getLabel());
    }

    @Test
    public void findByApplicationTest(){
        configService.findByApplication("test");
        verify(configRepository,times(1)).findByApplication(anyString(),any(Sort.class));
    }

    @Test
    public void findByApplicationAndProfileTest(){
        configService.findByApplicationAndProfile("test","test");
        verify(configRepository,times(1)).findByApplicationAndProfile(anyString(),anyString(),any(Sort.class));
    }
    @Test
    public void refreshConfigTest(){
        String serviceId="msb-test";
        String host="localhost";
        int port=wiremock.port();
        boolean secure=false;
        List<ServiceInstance> instances = Lists.newArrayList();
        InstanceInfo.PortWrapper portWrapper=new InstanceInfo.PortWrapper(false,port);
        InstanceInfo instanceInfo = new InstanceInfo(
                serviceId,
                serviceId,
                serviceId,
                host,
                "sid",
                portWrapper,
                portWrapper,
                "",
                "",
                "","","","",1,null,"",null,null,null,true,null,
                10l,10l,null,""

        );
        EurekaDiscoveryClient.EurekaServiceInstance ds =
                new EurekaDiscoveryClient.EurekaServiceInstance(instanceInfo);
        instances.add(ds);

        when(discoveryClient.getInstances(anyString())).thenReturn(instances);
        when(myProperties.getRefreshPath()).thenReturn("refresh");
        final String testUrl = "/refresh";
        stubFor(post(urlEqualTo(testUrl))
//                        .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("success")));

         configService.refreshConfig("msb-test");

         verify(discoveryClient,times(1)).getInstances(anyString());

    }
}
