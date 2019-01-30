package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.FilterHelperService;
import com.netflix.zuul.context.RequestContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_URI_KEY;

/**
 * @author Yuan Chunhai
 * @Date 9/30/2018-1:34 PM
 */
public class SignPreFilterTest {

    @InjectMocks
    SignPreFilter signPreFilter;

    @Mock
    private FilterHelperService filterHelperService;

    @Mock
    private MsbZuulProperties msbZuulProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_run() {

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.setRequest(mockHttpServletRequest);
        final String serviceId = "msb-api";
        requestContext.set(FilterConstants.SERVICE_ID_KEY, serviceId);
        int apiId = 1;
        requestContext.set(Constant.API_ID, apiId);
        requestContext.set(REQUEST_URI_KEY, "/apis");

        when(filterHelperService.getServiceSignKey(serviceId)).thenReturn(serviceId);

        signPreFilter.run();


        assertEquals("cb32086cdef2d7ce2d7e99007f7f7f0b3143bc33",
                requestContext.getZuulRequestHeaders().get("sign"));

    }

}
