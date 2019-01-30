package com.gmcc.msb.msbservice.service;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbservice.entity.ServiceLog;
import com.gmcc.msb.msbservice.properties.AppProperties;
import com.gmcc.msb.msbservice.repository.ServiceLogRepository;
import com.gmcc.msb.msbservice.vo.ServiceLogResp;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.when;

public class ServiceLogServiceTests {

    @InjectMocks
    ServiceLogService serviceLogService;

    @Mock
    AppProperties appProperties;

    @Mock
    ServiceLogRepository repository;

    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(
            WireMockSpring.options().dynamicPort());


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFind() {
        List<Long> ids = Lists.newArrayList(1l);
        UserContextHolder.getContext().setDataOrgList(ids);
        when(repository.findServiceLogByOrgs(any(),any(Pageable.class)))
                .thenReturn(null);

        assertNull(serviceLogService.find(new PageRequest(1, 2)));
    }

    @Test
    public void testAdd() {

        Long time = 12323421234323L;
        String content = "conetent";
        when(repository.findOneByTimeEqualsAndContentEquals(time, content))
                .thenReturn(null);
        ServiceLog serviceLog = new ServiceLog(time, 1, "sid", content);
        when(repository.save(serviceLog))
                .thenReturn(serviceLog);

        assertEquals(serviceLog, serviceLogService.add(serviceLog));

    }


    @Test
    public void testGetEurekaRegisterLog() throws IOException {

        int port = wiremock.port();

        stubFor(get(urlEqualTo("/register_log"))
//                        .withHeader("Accept", equalTo("text/xml"))
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("{\n" +
                                                              "    \"lastNCanceled\": [\n" +
                                                              "        {\n" +
                                                              "            \"date\": 1534127253548,\n" +
                                                              "            \"id\": \"MSB-SERVICE(GZCDC101282.dir.svc.accenture.com:msb-service:0)\"\n" +
                                                              "        },\n" +
                                                              "        {\n" +
                                                              "            \"date\": 1534127253032,\n" +
                                                              "            \"id\": \"MSB-SERVICE(GZCDC101282.dir.svc.accenture.com:msb-service:0)\"\n" +
                                                              "        }\n" +
                                                              "    ],\n" +
                                                              "    \"lastNRegistered\": [\n" +
                                                              "        {\n" +
                                                              "            \"date\": 1534127196390,\n" +
                                                              "            \"id\": \"MSB-SERVICE(GZCDC101282.dir.svc.accenture.com:msb-service:0)\"\n" +
                                                              "        },\n" +
                                                              "        {\n" +
                                                              "            \"date\": 1534127195867,\n" +
                                                              "            \"id\": \"MSB-SERVICE(GZCDC101282.dir.svc.accenture.com:msb-service:0)\"\n" +
                                                              "        },\n" +
                                                              "        {\n" +
                                                              "            \"date\": 1534126442340,\n" +
                                                              "            \"id\": \"MSB-CONFIG(GZCDC101282.dir.svc.accenture.com:msb-config:8888)\"\n" +
                                                              "        },\n" +
                                                              "        {\n" +
                                                              "            \"date\": 1534126441050,\n" +
                                                              "            \"id\": \"MSB-CONFIG(GZCDC101282.dir.svc.accenture.com:msb-config:8888)\"\n" +
                                                              "        }\n" +
                                                              "    ]\n" +
                                                              "}")));

        when(appProperties.getEurekaRegisterLogUrl())
                .thenReturn("http://localhost:" + port + "/register_log");
        when(appProperties.getRequestEurekaConnectionTime())
                .thenReturn(1000);
        when(appProperties.getRequestEurekaReadTime())
                .thenReturn(1000);


        ServiceLogResp resp = serviceLogService.getEurekaRegisterLog();

        assertNotNull(resp);
        assertEquals(2, resp.getLastNCanceled().size());
        assertEquals(4, resp.getLastNRegistered().size());

    }


    @Test(expected = MsbException.class)
    public void testGetEurekaRegisterLogNetError() {


        when(appProperties.getEurekaRegisterLogUrl())
                .thenReturn("http://localhost:9000/register_log");
        when(appProperties.getRequestEurekaConnectionTime())
                .thenReturn(1000);
        when(appProperties.getRequestEurekaReadTime())
                .thenReturn(1000);

        serviceLogService.getEurekaRegisterLog();

    }


    @Test(expected = MsbException.class)
    public void testGetEurekaRegisterLogReturn404() {

        int port = wiremock.port();

        stubFor(get(urlEqualTo("/register_log"))
//                        .withHeader("Accept", equalTo("text/xml"))
                        .willReturn(aResponse()
                                            .withStatus(404)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("")));

        when(appProperties.getEurekaRegisterLogUrl())
                .thenReturn("http://localhost:" + port + "/register_log");
        when(appProperties.getRequestEurekaConnectionTime())
                .thenReturn(1000);
        when(appProperties.getRequestEurekaReadTime())
                .thenReturn(1000);


        ServiceLogResp resp = serviceLogService.getEurekaRegisterLog();


    }

    @Test(expected = MsbException.class)
    public void testGetEurekaRegisterLogJsonParseError() {

        int port = wiremock.port();

        stubFor(get(urlEqualTo("/register_log"))
//                        .withHeader("Accept", equalTo("text/xml"))
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "application/json")
                                            .withBody("{\":hello\",\"world\"}")));

        when(appProperties.getEurekaRegisterLogUrl())
                .thenReturn("http://localhost:" + port + "/register_log");
        when(appProperties.getRequestEurekaConnectionTime())
                .thenReturn(1000);
        when(appProperties.getRequestEurekaReadTime())
                .thenReturn(1000);


        ServiceLogResp resp = serviceLogService.getEurekaRegisterLog();


    }


}
