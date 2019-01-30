package com.gmcc.msb.zuul.service;

import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.netflix.zuul.context.RequestContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_URI_KEY;

/**
 * @author Yuan Chunhai
 * @Date 9/30/2018-10:54 AM
 */
public class FilterHelperServiceTest {

    @InjectMocks
    FilterHelperService filterHelperService;

    @Mock
    private MsbZuulProperties msbZuulProperties;

    @Mock
    private SignKeyCache signKeyCache;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSkipTokenCheck_true() {

        when(msbZuulProperties.getSkipCheckTokenUri())
                .thenReturn(Arrays.asList("serviceid:/uri"));

        RequestContext requestContext = RequestContext.getCurrentContext();

        requestContext.set(REQUEST_URI_KEY, "/uri");
        requestContext.set(FilterConstants.SERVICE_ID_KEY, "serviceid");


        assertTrue(filterHelperService.skipTokenCheck());

    }

    @Test
    public void testSkipTokenCheck_false() {

        when(msbZuulProperties.getSkipCheckTokenUri())
                .thenReturn(Arrays.asList("msb-api:/uri"));

        RequestContext requestContext = RequestContext.getCurrentContext();

        requestContext.set(REQUEST_URI_KEY, "/uri");
        requestContext.set(FilterConstants.SERVICE_ID_KEY, "serviceid");


        assertFalse(filterHelperService.skipTokenCheck());

    }

    @Test
    public void testgetServiceSignKey() {

        filterHelperService.getServiceSignKey("msb-api");

    }


}
