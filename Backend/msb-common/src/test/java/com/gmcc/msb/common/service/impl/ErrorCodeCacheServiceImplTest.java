package com.gmcc.msb.common.service.impl;

import com.gmcc.msb.common.entity.ErrorCode;
import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.service.client.MsbServiceClient;
import com.gmcc.msb.common.vo.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Yuan Chunhai
 * @Date 10/18/2018-4:25 PM
 */
public class ErrorCodeCacheServiceImplTest {


    @InjectMocks
    ErrorCodeCacheServiceImpl errorCodeCacheService;

    @Mock
    private MsbServiceClient msbServiceClient;
    @Mock
    private MsbProperties msbProperties;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() {

        Response<List<ErrorCode>> resp = new Response<>();
        List<ErrorCode> codes = Arrays.asList(new ErrorCode("0000-00001", "msg", "null"));
        resp.setContent(codes);
        when(msbServiceClient.findAllInService(null)).thenReturn(resp);

        errorCodeCacheService.refreshCache();


        ErrorCode result = errorCodeCacheService.getErrorCode("0000-00001");
        assertNotNull(result);

    }

}
