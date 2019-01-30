package com.gmcc.msb.zuul.service;

import com.gmcc.msb.zuul.entity.AppOrderApi;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.repository.AppOrderApiRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Yuan Chunhai
 * @Date 9/30/2018-10:03 AM
 */
public class AppOrderApiServiceTest {


    @InjectMocks
    AppOrderApiService appOrderApiService;


    @Mock
    private AppOrderApiRepository repository;

    @Mock
    private MsbZuulProperties msbZuulProperties;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isAppOrderApiTrue(){

        when(repository.findAllAvaiableOrder(1, 1))
                .thenReturn(Arrays.asList(new AppOrderApi()));

        assertTrue(appOrderApiService.isAppOrderApi(1,1));

    }

    @Test
    public void isAppOrderApiFalse(){

        when(repository.findAllAvaiableOrder(1, 1))
                .thenReturn(null);

        assertFalse(appOrderApiService.isAppOrderApi(1,1));

    }

}
