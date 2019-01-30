package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.entity.App;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.AppService;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.monitoring.CounterFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.metrics.repository.InMemoryMetricRepository;
import org.springframework.boot.actuate.metrics.writer.DefaultCounterService;
import org.springframework.cloud.netflix.zuul.metrics.DefaultCounterFactory;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Yuan Chunhai
 * @Date 9/30/2018-11:21 AM
 */
public class CheckAppStatusPreFilterTest {

    @InjectMocks
    CheckAppStatusPreFilter checkAppStatusPreFilter;

    @Mock
    private MsbZuulProperties msbZuulProperties;

    @Mock
    private AppService appService;

    @Rule
    public ExpectedException expected = ExpectedException.none();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CounterFactory.initialize(new DefaultCounterFactory(new DefaultCounterService(
                new InMemoryMetricRepository())));
    }

    @Test
    public void test_empty_appid() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-00002");

        when(msbZuulProperties.getHeaderAppIdKey()).thenReturn("api-id");

        RequestContext requestContext = RequestContext.getCurrentContext();

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
//        mockRequest.addHeader("api-id", "msb_gmcc_net");

        requestContext.setRequest(mockRequest);
        checkAppStatusPreFilter.run();
    }

    @Test
    public void test_invalid_app() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-10012");

        String appIdKey = "api-id";
        final String appid = "msb_gmcc_net";

        when(msbZuulProperties.getHeaderAppIdKey()).thenReturn(appIdKey);
        when(appService.isAppAvaiable(appid)).thenThrow(new MsbException("0008-10012"));


        RequestContext requestContext = RequestContext.getCurrentContext();

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
        mockRequest.addHeader(appIdKey, appid);

        requestContext.setRequest(mockRequest);
        checkAppStatusPreFilter.run();
    }

    @Test
    public void test_run() {

        String appIdKey = "api-id";
        final String appid = "msb_gmcc_net";

        App app = new App();
        app.setAppId(appid);
        app.setId(1);

        when(msbZuulProperties.getHeaderAppIdKey()).thenReturn(appIdKey);
        when(appService.isAppAvaiable(appid))
                .thenReturn(app);


        RequestContext requestContext = RequestContext.getCurrentContext();

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("GET", "/uri");
        mockRequest.addHeader(appIdKey, appid);

        requestContext.setRequest(mockRequest);
        checkAppStatusPreFilter.run();

        assertEquals(app.getId(), requestContext.get(Constant.APP_ID));

    }


}
