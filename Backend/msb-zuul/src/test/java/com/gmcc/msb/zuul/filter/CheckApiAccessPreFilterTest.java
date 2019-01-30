package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.entity.ApiInfo;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
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

/**
 * @author Yuan Chunhai
 * @Date 9/30/2018-1:51 PM
 */
public class CheckApiAccessPreFilterTest {


    @InjectMocks
    CheckApiAccessPreFilter checkApiAccessPreFilter;

    @Mock
    private MsbZuulProperties properties;


    @Rule
    public ExpectedException expected = ExpectedException.none();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        CounterFactory.initialize(new DefaultCounterFactory(new DefaultCounterService(
                new InMemoryMetricRepository())));

    }


    @Test
    public void test_run() {


        RequestContext requestContext = RequestContext.getCurrentContext();

        ApiInfo targetApi = new ApiInfo();
        targetApi.setAnnoymousAccess(true);
        requestContext.set(Constant.API_INFO, targetApi);

        requestContext.set(Constant.USER_ID, "");

        checkApiAccessPreFilter.run();


    }

    @Test
    public void test_run_ex() {

        expected.expect(ZuulRuntimeException.class);
        expected.expectMessage("0008-10007");

        RequestContext requestContext = RequestContext.getCurrentContext();

        ApiInfo targetApi = new ApiInfo();
        targetApi.setAnnoymousAccess(false);
        requestContext.set(Constant.API_INFO, targetApi);

        requestContext.set(Constant.USER_ID, "");

        checkApiAccessPreFilter.run();


    }
}
