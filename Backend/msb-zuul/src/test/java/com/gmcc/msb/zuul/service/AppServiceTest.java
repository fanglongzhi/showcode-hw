package com.gmcc.msb.zuul.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.zuul.entity.App;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.repository.AppRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Yuan Chunhai
 * @Date 9/20/2018-1:14 PM
 */
public class AppServiceTest {


    @InjectMocks
    private AppService appService;

    @Mock
    private AppRepository appRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private MsbZuulProperties msbZuulProperties;


    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByAppId(){

        App app = new App();
        when(appRepository.findOneByAppIdEquals("1"))
                .thenReturn(app);
        when(redisService.getAppByAppId("1"))
                .thenReturn(app);

        App result = appService.findByAppId("1");
        assertEquals(app, result);

    }

    @Test
    public void testIsAppAvaiable_emptyApp(){

        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage("0008-10012");

        when(appRepository.findOneByAppIdEquals("1"))
                .thenReturn(null);
        when(redisService.getAppByAppId("1"))
                .thenReturn(null);

        appService.isAppAvaiable("1");
    }


    @Test
    public void testIsAppAvaiable_status_not_avaiable(){

        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage("0008-10013");

        App app = new App();
        when(appRepository.findOneByAppIdEquals("1"))
                .thenReturn(app);
        when(redisService.getAppByAppId("1"))
                .thenReturn(app);

        appService.isAppAvaiable("1");
    }


    @Test
    public void testIsAppAvaiable_status_not_avaiable_1(){

        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage("0008-10013");

        App app = new App();
        app.setStatus(0);
        when(appRepository.findOneByAppIdEquals("1"))
                .thenReturn(app);
        when(redisService.getAppByAppId("1"))
                .thenReturn(app);

        appService.isAppAvaiable("1");
    }


    @Test
    public void testIsAppAvaiable(){

        App app = new App();
        app.setStatus(1);
        when(appRepository.findOneByAppIdEquals("1"))
                .thenReturn(app);
        when(redisService.getAppByAppId("1"))
                .thenReturn(app);

        App result = appService.isAppAvaiable("1");
        assertNotNull(result);


    }

}
