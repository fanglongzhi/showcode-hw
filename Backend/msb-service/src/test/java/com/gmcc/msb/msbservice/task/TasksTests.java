package com.gmcc.msb.msbservice.task;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbservice.entity.ServiceLog;
import com.gmcc.msb.msbservice.service.ServiceLogService;
import com.gmcc.msb.msbservice.vo.ServiceLogResp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TasksTests {


    @InjectMocks
    Tasks tasks;


    @Mock
    ServiceLogService serviceLogService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSyncServiceLogTask() {

        when(serviceLogService.getEurekaRegisterLog())
                .thenThrow(new MsbException("网络异常"));

        tasks.syncServiceLogTask();

    }

    @Test
    public void testSyncServiceLogTask2() {

        ServiceLogResp resp = new ServiceLogResp();
        resp.setLastNRegistered(Arrays.asList(new ServiceLogResp.Data(123, "1")));
        resp.setLastNCanceled(Arrays.asList(new ServiceLogResp.Data(234, "2")));


        when(serviceLogService.getEurekaRegisterLog())
                .thenReturn(resp);
        when(serviceLogService.add(any(ServiceLog.class)))
                .thenReturn(null);

        tasks.syncServiceLogTask();

    }


}
