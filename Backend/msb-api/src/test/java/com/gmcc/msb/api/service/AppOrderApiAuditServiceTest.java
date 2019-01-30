package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppOrderApiAudit;
import com.gmcc.msb.api.repository.ApiRepository;
import com.gmcc.msb.api.repository.AppOrderApiAuditRepository;
import com.gmcc.msb.api.repository.AppRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AppOrderApiAuditServiceTest {
    @Mock
    private AppOrderApiAuditRepository repository;
    @Mock
    private AppRepository appRepository;
    @Mock
    private ApiRepository apiRepository;

    @InjectMocks
    private AppOrderApiAuditService service;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void test_findAll(){
        int id = 1;
        AppOrderApiAudit audit = new AppOrderApiAudit();
        audit.setId(id);
        ArrayList<AppOrderApiAudit> list = new ArrayList<>();
        list.add(audit);
        when(repository.findAll())
                .thenReturn(list);
        List<AppOrderApiAudit> all = service.findAll();
        assertTrue(all != null);
        assertTrue(all.size()>0);
    }

    @Test
    public void  test_delete(){
        service.delete(1);
        verify(repository,times(1)).delete(1);
    }

    @Test
    public void test_sava(){
        int id = 1;
        AppOrderApiAudit audit = new AppOrderApiAudit();
        audit.setId(id);
        audit.setAppId(1);
        audit.setApiId(1);
        when(repository.save(audit)).thenReturn(audit);
        when(apiRepository.findOne(1)).thenReturn(new API());
        when(appRepository.findOne(1)).thenReturn(new App());
        AppOrderApiAudit sava = service.save(audit);
        verify(repository,times(1)).save(audit);
    }

    @Test
    public void test_findOne(){
        int id = 1;
        AppOrderApiAudit audit = new AppOrderApiAudit();
        audit.setId(id);
        when(repository.findOne(id)).thenReturn(audit);
        AppOrderApiAudit one = service.findOne(id);
        verify(repository,times(1)).findOne(id);
        assertTrue(one.getId()==id);
    }

    @Test
    public void testFindByOrderIdAndStatus(){
        int id = 1;
        int appId = 11;
        int orderId = 111;
        int status = 0;
        AppOrderApiAudit audit = new AppOrderApiAudit();
        audit.setId(id);
        audit.setAppId(appId);
        audit.setApiId(1);
        audit.setOrderId(orderId);

        ArrayList<AppOrderApiAudit> list = new ArrayList<>();
        list.add(audit);
        when(repository.findByOrderId(orderId)).thenReturn(list);
        List<AppOrderApiAudit> appIdAndTypeId = service.findByOrderIdAndStatus(orderId);
        verify(repository).findByOrderId(orderId);
        assertTrue(appIdAndTypeId.get(0).getId()==id);
    }
}
