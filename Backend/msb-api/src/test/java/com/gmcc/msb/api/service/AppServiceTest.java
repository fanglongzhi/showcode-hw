package com.gmcc.msb.api.service;

import com.gmcc.msb.api.common.CommonConstant;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.repository.AppOrderApiRepository;
import com.gmcc.msb.api.repository.AppRepository;
import com.gmcc.msb.api.vo.request.AppRequest;
import com.gmcc.msb.api.vo.request.ModifyAppRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.common.service.client.MsbFluidClient;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.xml.ws.Response;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

public class AppServiceTest {
    @Mock
    private AppRepository appRepository;
    @Mock
    private AppOrderApiRepository appOrderApiRepository;
    @Mock
    private AppAuditService appAuditService;
    @Mock
    private MsbFluidClient msbFluidClient;
    @Mock
    private RedisService redisService;

    @InjectMocks
    private AppService appService;
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testAppsOrderApiId(){

        appService.appsOrderApiId(1,new PageRequest(1,100));
        verify(appRepository,times(1)).appsOrderedApiId(anyInt(),anyObject());
    }
    @Test
    public void testCreateApp(){
        AppRequest param = new AppRequest();
        param.setAppName("testApp");

        App app = new App(param);
        app.setAppId(UUID.randomUUID().toString());
        app.setStatus(0);

        when(appRepository.save(any(App.class))).thenReturn(app);

        App newApp = appService.createApplication(param);

        assertThat(newApp).isNotNull();
        Assert.assertNotNull(newApp.getAppId());
    }

    @Test
    public void testCountAppName(){
        appService.countAppName("test");
        verify(appRepository,times(1)).countByAppName(any());
    }
    @Test
    public void testFindAllApp(){
        List<App> list = Lists.newArrayList();
        UserContextHolder.getContext().setDataOrgList(Lists.newArrayList(1l));
        when(appRepository.findAppByOrgList(any(List.class),any(Sort.class))).thenReturn(list);
        appService.findAllApp();
        verify(appRepository,times(1)).findAppByOrgList(any(List.class),any(Sort.class));
    }
    @Test
    public void testOneApp(){
        appService.findOneApp(1);
        verify(appRepository,times(1)).findOne(1);
    }
    @Test
    public void testDeleteApp_fail_hasSubscribe(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(CommonConstant.ERROR_APP_HAS_SUBSCRIBE_CODE);
        when(appOrderApiRepository.countByAppId(anyInt())).thenReturn(1L);
        appService.deleteApp(1);
    }
    @Test
    public void testDeleteApp_fail_noExist(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(CommonConstant.ERROR_RECORD_NOT_EXIST_CODE);
        when(appOrderApiRepository.countByAppId(anyInt())).thenReturn(0l);
        when(appRepository.findOne(anyInt())).thenReturn(null);
        appService.deleteApp(1);


    }
    @Test
    public void testDeleteApp_fail_wrongStatus(){
        App app = new App();
        app.setStatus(App.STATUS_AUDITING);
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(CommonConstant.ERROR_APP_AUDITING_CODE);
        when(appOrderApiRepository.countByAppId(anyInt())).thenReturn(0l);
        when(appRepository.findOne(anyInt())).thenReturn(app);
        appService.deleteApp(1);

    }

    @Test
    public void testDeleteApp_success(){
        App app = new App();
        app.setStatus(App.STATUS_AUDIT_FAIL);
        when(appOrderApiRepository.countByAppId(anyInt())).thenReturn(0l);
        when(appRepository.findOne(anyInt())).thenReturn(app);

        com.gmcc.msb.common.vo.Response<Boolean> res= new com.gmcc.msb.common.vo.Response();
        res.setContent(false);
        when(msbFluidClient.judgeStrategyExist("app",1)).thenReturn(res);
        appService.deleteApp(1);
        verify(appRepository,times(1)).delete(1);

    }
    @Test
    public void testApplyApp_fail_noExist(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(CommonConstant.ERROR_RECORD_NOT_EXIST_CODE);
        when(appRepository.findOne(anyInt())).thenReturn(null);
        appService.applyApp(1);
    }
    @Test
    public void testApplyApp_fail_wrongStatus(){
        App app = new App();
        app.setStatus(App.STATUS_AVAILABLE);

        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(CommonConstant.ERROR_WRONG_STATUS_CODE);

        when(appRepository.findOne(1)).thenReturn(app);

        appService.applyApp(1);

    }
    @Test
    public void testApplyApp_success(){
        App app = new App();
        app.setStatus(App.STATUS_NEW);
        when(appRepository.findOne(1)).thenReturn(app);
        appService.applyApp(1);
        verify(appRepository,times(1)).save(any(App.class));
    }

    @Test
    public void testModifyApp_fail_no_exist(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(CommonConstant.ERROR_RECORD_NOT_EXIST_CODE);
        when(appRepository.findOne(anyInt())).thenReturn(null);
        appService.modifyApp(1,new ModifyAppRequest());

    }

    @Test
    public void testModifyApp_fail_wrongStatus(){
        App app = new App();
        app.setStatus(App.STATUS_AVAILABLE);

        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(CommonConstant.ERROR_WRONG_STATUS_CODE);

        when(appRepository.findOne(1)).thenReturn(app);

        appService.modifyApp(1,new ModifyAppRequest());

    }
    @Test
    public void testModifyApp_success(){
        App app = new App();
        app.setStatus(App.STATUS_NEW);
        when(appRepository.findOne(1)).thenReturn(app);
        ModifyAppRequest appRequest = new ModifyAppRequest();
        appRequest.setCompany("company");
        appRequest.setLinkEmail("email");
        appRequest.setLinkMan("man");
        appRequest.setLinkTel("tel");
        appRequest.setDescription("desc");
        appRequest.setAppName("testName");
        appService.modifyApp(1,appRequest);
        verify(appRepository,times(1)).save(any(App.class));
    }

    @Test
    public void testFindApiOfAppSubscribe(){
        appService.findApiOfAppSubscribe(1);
        verify(appRepository,times(1)).findApiOfAppSubscribe(1);
    }

}
