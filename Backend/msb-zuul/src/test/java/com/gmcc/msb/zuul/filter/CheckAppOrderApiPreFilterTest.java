package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.AppOrderApiService;
import com.gmcc.msb.zuul.service.RedisService;
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

import static org.mockito.Mockito.when;

/**
 * @author Yuan Chunhai
 * @Date 9/30/2018-11:21 AM
 */
public class CheckAppOrderApiPreFilterTest {

    @InjectMocks
    CheckAppOrderApiPreFilter checkAppOrderApiPreFilter;

    @Mock
    private MsbZuulProperties msbZuulProperties;

    @Mock
    private AppOrderApiService appOrderApiService;

    @Mock
    private RedisService redisService;



    @Rule
    public ExpectedException expected = ExpectedException.none();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CounterFactory.initialize(new DefaultCounterFactory(new DefaultCounterService(
                new InMemoryMetricRepository())));
    }

    @Test
    public void test_unorder() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-10010");

        when(appOrderApiService.isAppOrderApi(1, 1))
                .thenReturn(false);
        when(appOrderApiService.isAppOrderApiFromRedis(1, 1))
                .thenReturn(false);

        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.set(Constant.APP_ID, 1);
        requestContext.set(Constant.API_ID, 1);

        checkAppOrderApiPreFilter.run();

    }

    @Test
    public void test_order() {

        when(appOrderApiService.isAppOrderApi(1, 1))
                .thenReturn(true);
        when(appOrderApiService.isAppOrderApiFromRedis(1, 1))
                .thenReturn(true);

        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.set(Constant.APP_ID, 1);
        requestContext.set(Constant.API_ID, 1);

        checkAppOrderApiPreFilter.run();

    }




}
