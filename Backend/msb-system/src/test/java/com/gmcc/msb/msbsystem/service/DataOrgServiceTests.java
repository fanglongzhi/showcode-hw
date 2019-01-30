package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.entity.org.DataOrg;
import com.gmcc.msb.msbsystem.repository.org.DataOrgRepository;
import com.gmcc.msb.msbsystem.vo.req.org.CreateDataOrgParam;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @program: msb-system
 * @description: 数据组SERVICE UT
 * @author: zhifanglong
 * @create: 2018-10-30 11:52
 */
public class DataOrgServiceTests {
    private static final String ORG_NAME_EXIST="数据组已经存在";

    private static final String PARENT_ORG_NOT_EXIST="父组不存在";

    private static final String ORG_NOT_EXIST="0007-10003";

    private static final String ORG_HAS_USER="0007-10004";
    @InjectMocks
    private DataOrgService dataOrgService;
    @Mock
    private DataOrgRepository dataOrgRepository;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateDataOrg(){
        DataOrg parent = new DataOrg();
        when(dataOrgRepository.countByOrgName("test")).thenReturn(0);
        when(dataOrgRepository.findOne(1)).thenReturn(parent);
        when(dataOrgRepository.save(any(DataOrg.class))).thenReturn(parent);

        CreateDataOrgParam param = new CreateDataOrgParam();
        param.setOrgName("test");
        param.setParent(1);
        DataOrg result = dataOrgService.createDataOrg(param);

        Assert.assertNotNull(result);
    }
    @Test
    public void testCreateDataOrg_duplicate(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage("0007-10001");
        when(dataOrgRepository.countByOrgName("test")).thenReturn(1);
        CreateDataOrgParam param = new CreateDataOrgParam();
        param.setOrgName("test");
        param.setParent(1);
        DataOrg result = dataOrgService.createDataOrg(param);
    }


}
