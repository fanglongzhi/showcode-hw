package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppAudit;
import com.gmcc.msb.api.repository.AppAuditRepository;
import com.gmcc.msb.api.repository.AppRepository;
import com.gmcc.msb.api.vo.request.AuditRequest;
import com.gmcc.msb.common.exception.MsbException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.TestCase.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class AppAuditServiceTest {


    @InjectMocks
    AppAuditService appAuditService;

    @Mock
    AppAuditRepository appAuditRepository;

    @Mock
    AppRepository appRepository;

    @Mock
    private RedisService redisService;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        when(appAuditRepository.save(any(AppAudit.class)))
                .thenReturn(null);


        App app = new App();
        app.setId(1);
        assertNull(appAuditService.create(app));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAuditEmptyParams(){

        appAuditService.audit(null, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testAuditEmptyNoRecord(){
        when(appAuditRepository.findOne(1)).thenReturn(null);
        appAuditService.audit(1, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAuditEmptyNoApp(){

        AppAudit appAudit = new AppAudit();
        appAudit.setAppId(1);
        when(appAuditRepository.findOne(1)).thenReturn(appAudit);

        when(appRepository.findOne(1)).thenReturn(null);

        appAuditService.audit(1, null);


    }

    @Test(expected = MsbException.class)
    public void testAuditEmptyAppStatusError(){

        AppAudit appAudit = new AppAudit();
        appAudit.setAppId(1);
        when(appAuditRepository.findOne(1)).thenReturn(appAudit);

        App app = new App();
        app.setStatus(1);
        when(appRepository.findOne(1)).thenReturn(app);

        AuditRequest ar = new AuditRequest();
        ar.setResult(1);
        appAuditService.audit(1, ar);


    }


    @Test
    public void testAuditPass(){

        AppAudit appAudit = new AppAudit();
        appAudit.setAppId(1);
        when(appAuditRepository.findOne(1)).thenReturn(appAudit);

        App app = new App();
        app.setStatus(2);
        when(appRepository.findOne(1)).thenReturn(app);

        when(appRepository.save(any(App.class))).thenReturn(app);
        when(appAuditRepository.save(any(AppAudit.class))).thenReturn(null);


        AuditRequest ar = new AuditRequest();
        ar.setResult(0);
        appAuditService.audit(1, ar);

        assertEquals(App.STATUS_AVAILABLE, app.getStatus().intValue());
        assertNotNull(app.getAppSecret());

    }

    @Test
    public void testAuditnotPass(){

        AppAudit appAudit = new AppAudit();
        appAudit.setAppId(1);
        when(appAuditRepository.findOne(1)).thenReturn(appAudit);

        App app = new App();
        app.setStatus(2);
        when(appRepository.findOne(1)).thenReturn(app);

        when(appRepository.save(any(App.class))).thenReturn(app);
        when(appAuditRepository.save(any(AppAudit.class))).thenReturn(null);


        AuditRequest ar = new AuditRequest();
        ar.setResult(1);
        appAuditService.audit(1, ar);

        assertEquals(App.STATUS_AUDIT_FAIL, app.getStatus().intValue());
        assertNull(app.getAppSecret());
    }

}
