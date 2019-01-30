package com.gmcc.msb.msbservice.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbservice.entity.ErrorCode;
import com.gmcc.msb.msbservice.entity.ServiceItem;
import com.gmcc.msb.msbservice.repository.ErrorCodeRepository;
import com.gmcc.msb.msbservice.repository.ServiceItemRepository;
import com.gmcc.msb.msbservice.vo.req.ErrorCodeReq;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Yuan Chunhai
 * @Date 10/12/2018-3:42 PM
 */
public class ErrorCodeServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @InjectMocks
    ErrorCodeService errorCodeService;
    @Mock
    private ErrorCodeRepository errorCodeRepository;
    @Mock
    private ServiceItemRepository serviceItemRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddServiceNotExists() {

        exception.expect(MsbException.class);
        exception.expectMessage("0006-10005");

        String serviceId = "api";
        when(serviceItemRepository.findOneByServiceIdEquals(serviceId))
                .thenReturn(null);

        ErrorCodeReq errorcode = new ErrorCodeReq();
        errorCodeService.add(errorcode);

    }

    @Test
    public void testAddServiceCodeNotExists() {

        exception.expect(MsbException.class);
        exception.expectMessage("0006-10006");

        String serviceId = "api";
        when(serviceItemRepository.findOneByServiceIdEquals(serviceId))
                .thenReturn(new ServiceItem());

        ErrorCodeReq errorcode = new ErrorCodeReq();
        errorcode.setServiceId(serviceId);
        errorCodeService.add(errorcode);

    }

    @Test
    public void testAddServiceCodeExists() {

        exception.expect(MsbException.class);
        exception.expectMessage("0006-10001");

        String serviceId = "api";
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceCode("0001");
        when(serviceItemRepository.findOneByServiceIdEquals(serviceId))
                .thenReturn(serviceItem);
        when(errorCodeRepository.findOneByCodeEquals("0001-" + "10000"))
                .thenReturn(new ErrorCode());

        ErrorCodeReq errorcode = new ErrorCodeReq();
        errorcode.setServiceId(serviceId);
        errorcode.setCode("10000");
        errorCodeService.add(errorcode);

    }

    @Test
    public void testAdd() {


        String serviceId = "api";
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setServiceCode("0001");
        when(serviceItemRepository.findOneByServiceIdEquals(serviceId))
                .thenReturn(serviceItem);
        when(errorCodeRepository.findOneByCodeEquals("0001-" + "10000"))
                .thenReturn(null);


        ErrorCodeReq errorcode = new ErrorCodeReq();
        errorcode.setServiceId(serviceId);
        errorcode.setCode("10000");
        errorcode.setMessage("msg");
        errorcode.setDescription("desc");
        ErrorCode result = errorCodeService.add(errorcode);
    }


    @Test
    public void testUpdateNotExist() {

        exception.expect(MsbException.class);
        exception.expectMessage("0006-10002");

        when(errorCodeRepository.findOne(1))
                .thenReturn(null);


        errorCodeService.update(1, new ErrorCodeReq());

    }

    @Test
    public void testUpdate() {


        ErrorCode errorCode = new ErrorCode();
        errorCode.setId(2);
        errorCode.setCode("0001");
        errorCode.setServiceId("api");
        errorCode.setMessage("入参不能为空");
        errorCode.setDescription("描述");
        errorCode.setCreateDate(new Date());
        errorCode.setUpdateDate(new Date());

        when(errorCodeRepository.findOne(1))
                .thenReturn(errorCode);


        ErrorCodeReq errorCode1 = new ErrorCodeReq();
        errorCode1.setCode("klk");
        errorCode1.setMessage("mmm");
        errorCode1.setDescription("ddd");


        errorCodeService.update(1, errorCode1);

        assertEquals(2, errorCode.getId().intValue());
        assertEquals("0001", errorCode.getCode());
        assertEquals("api", errorCode.getServiceId());

        assertEquals(errorCode1.getMessage(), errorCode.getMessage());
        assertEquals(errorCode1.getDescription(), errorCode.getDescription());
    }


    @Test
    public void testDeleteRecordNotExists() {

        exception.expect(MsbException.class);
        exception.expectMessage("0006-10003");

        when(errorCodeRepository.findOne(1))
                .thenReturn(null);

        errorCodeService.delete(1);
    }

    @Test
    public void testDelete() {


        ErrorCode errorCode = new ErrorCode();
        when(errorCodeRepository.findOne(1))
                .thenReturn(errorCode);

        errorCodeService.delete(1);
    }


    @Test
    public void testFindRecordNotExists() {

        exception.expect(MsbException.class);
        exception.expectMessage("0006-10004");

        when(errorCodeRepository.findOne(1))
                .thenReturn(null);

        errorCodeService.find(1);
    }

    @Test
    public void testFindRecord() {


        when(errorCodeRepository.findOne(1))
                .thenReturn(new ErrorCode());

        errorCodeService.find(1);
    }


}
