package com.gmcc.msb.msbsystem.service;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.gmcc.msb.msbsystem.property.AppProperties;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;

public class EOMSServiceTests {

    @InjectMocks
    EOMSService eomsService;

    @Mock
    AppProperties appProperties;

    @Mock
    OrgService orgService;


    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(
            WireMockSpring.options().dynamicPort());


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSync() {

        int port = wiremock.port();

        final String testUrl = "/eomsurl";
        stubFor(post(urlEqualTo(testUrl))
//                        .withHeader("Accept", equalTo("text/xml"))
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withBodyFile("eoms_resp/orgs.json")));

        when(appProperties.getEomsServiceUrl()).thenReturn("http://localhost:" + port + testUrl);

        this.eomsService.sync("orgid,orgname,parentorgid");

    }

}
