package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.entity.API;
import com.gmcc.msb.msbsystem.entity.module.Module;
import com.gmcc.msb.msbsystem.repository.ApiRepository;
import com.gmcc.msb.msbsystem.repository.module.ModuleRepository;
import com.gmcc.msb.msbsystem.repository.role.RoleAuthRepository;
import com.gmcc.msb.msbsystem.vo.req.ModuleVo;
import com.gmcc.msb.msbsystem.vo.resp.ModuleRespVo;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.gmcc.msb.msbsystem.common.CommonConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * @program: msb-system
 * @description: 模块逻辑层UT
 * @author: zhifanglong
 * @create: 2018-10-11 15:38
 */
public class ModuleServiceTests {
    @InjectMocks
    private ModuleService moduleService;
    @Mock
    private ModuleRepository moduleRepository;
    @Mock
    private RoleAuthRepository roleAuthRepository;
    @Mock
    private ApiRepository apiRepository;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testCreateModule_key_duplicate(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_MODULE_KEY_DUPLICATE);

        ModuleVo moduleVo = new ModuleVo();
        moduleVo.setKey("test");

        when(moduleRepository.countByKey(moduleVo.getKey())).thenReturn(1);
        moduleService.createModule(moduleVo);
    }
    @Test
    public void testCreateModule_name_duplicate(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_MODULE_NAME_DUPLICATE);

        ModuleVo moduleVo = new ModuleVo();
        moduleVo.setModuleName("test");

        when(moduleRepository.countByModuleName(moduleVo.getModuleName())).thenReturn(1);
        moduleService.createModule(moduleVo);
    }

    @Test
    public void testCreateModule(){
        ModuleVo moduleVo = new ModuleVo();
        moduleVo.setModuleName("test");
        moduleVo.setKey("testKey");
        moduleVo.setApis(Lists.newArrayList(1,2,3));
        moduleVo.setParentId(0l);

        Module m = new Module();
        m.setId(10l);
        when(moduleRepository.countByKey(moduleVo.getKey())).thenReturn(0);
        when(moduleRepository.countByModuleName(moduleVo.getModuleName())).thenReturn(0);
        when(moduleRepository.save(any(Module.class))).thenReturn(m);


        Module module = moduleService.createModule(moduleVo);

        Assert.assertNotNull(module);
        Assert.assertEquals(moduleVo.getKey(),module.getKey());
        Assert.assertEquals(moduleVo.getModuleName(),module.getModuleName());
        Assert.assertEquals("1,2,3",module.getApis());
        Assert.assertEquals(moduleVo.getParentId(),module.getParentId());
        Assert.assertNotNull(module.getCreateTime());
        Assert.assertNotNull(module.getUpdateTime());
    }
    @Test
    public void testDeleteModule_not_exist(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_MODULE_NOT_EXIST);

        when(moduleRepository.findOne(1l)).thenReturn(null);

        moduleService.deleteModule(1l);
    }
    @Test
    public void testDeleteModule_has_auth(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_MODULE_HAS_ROLE);

        when(moduleRepository.findOne(1l)).thenReturn(new Module());
        when(roleAuthRepository.countByAuthId(1l)).thenReturn(1);

        moduleService.deleteModule(1l);
    }

    @Test
    public void testDeleteModule(){
        when(moduleRepository.findOne(1l)).thenReturn(new Module());
        when(roleAuthRepository.countByAuthId(1l)).thenReturn(0);

        Module result = moduleService.deleteModule(1l);

        Assert.assertNotNull(result);
    }
    @Test
    public void testFindAllModule(){
        List<Module> moduleList = Lists.newArrayList();

        Module m = new Module();
        m.setId(1l);
        m.setModuleName("m1");
        m.setKey("m1-key");
        m.setApis("1,2");
        moduleList.add(m);

        Module m2 = new Module();
        m2.setId(2l);
        m2.setModuleName("m2");
        m2.setKey("m2-key");
        m2.setApis("1,2");
        moduleList.add(m2);

        Module m3 = new Module();
        m3.setId(3l);
        m3.setModuleName("m3");
        m3.setKey("m3-key");
        moduleList.add(m3);

        API a1 = new API();
        a1.setApiName("api1");
        a1.setId(1);

        API a2 = new API();
        a2.setApiName("api2");
        a2.setId(2);


        when(moduleRepository.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(moduleList);

        when(apiRepository.findByIdIn(any(List.class))).thenReturn(Lists.newArrayList(a1,a2));

        List<Module> result=moduleService.findAllModule();

        Assert.assertNotNull(result);
        Assert.assertEquals(3,result.size());

        boolean hasM1=false,hasM2=false,hasM3=false;
        for(Module module:result){
            if(module.getId()==1){
                Assert.assertEquals(2,module.getApiList().size());
                hasM1=true;
            }
            if(module.getId()==2){
                Assert.assertEquals(2,module.getApiList().size());
                hasM2=true;
            }

            if(module.getId()==3){
                Assert.assertTrue(module.getApiList().isEmpty());
                hasM3=true;
            }
        }

        Assert.assertTrue(hasM1&&hasM2&&hasM3);
    }

    @Test
    public void testModifyModule_not_exist(){
        ModuleVo param = new ModuleVo();
        param.setId(1l);
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_MODULE_NOT_EXIST);

        when(moduleRepository.findOne(1l)).thenReturn(null);
        moduleService.modifyModule(param);
    }
    @Test
    public void testModifyModule_key_dumplicate(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_MODULE_KEY_DUPLICATE);

        ModuleVo moduleVo = new ModuleVo();
        moduleVo.setKey("test");
        moduleVo.setId(1l);
        when(moduleRepository.findOne(1l)).thenReturn(new Module());
        when(moduleRepository.countByKey(moduleVo.getKey(),moduleVo.getId())).thenReturn(1);
        moduleService.modifyModule(moduleVo);
    }

    @Test
    public void testModifyModule_name_dumplicate(){
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_MODULE_NAME_DUPLICATE);

        ModuleVo moduleVo = new ModuleVo();
        moduleVo.setKey("test");
        moduleVo.setId(1l);
        when(moduleRepository.findOne(1l)).thenReturn(new Module());
        when(moduleRepository.countByModuleName(moduleVo.getModuleName(),moduleVo.getId())).thenReturn(1);
        moduleService.modifyModule(moduleVo);
    }
    @Test
    public void testModifyModule(){
        ModuleVo moduleVo = new ModuleVo();
        moduleVo.setKey("test");
        moduleVo.setId(1l);
        when(moduleRepository.findOne(1l)).thenReturn(new Module());
        when(moduleRepository.countByKey(moduleVo.getKey(),moduleVo.getId())).thenReturn(0);
        when(moduleRepository.countByModuleName(moduleVo.getModuleName(),moduleVo.getId())).thenReturn(0);

        Module result = moduleService.modifyModule(moduleVo);

        Assert.assertNotNull(result);
    }

    @Test
    public void testModifyModuleStatus_not_exist(){
        ModuleVo param = new ModuleVo();
        param.setId(1l);
        expectedEx.expect(MsbException.class);
        expectedEx.expectMessage(ERROR_MODULE_NOT_EXIST);

        when(moduleRepository.findOne(1l)).thenReturn(null);
        moduleService.modifyModuleStatus(1l,"0");
    }

    @Test
    public void testModifyModuleStatus(){

        when(moduleRepository.findOne(1l)).thenReturn(new Module());
        Module result = moduleService.modifyModuleStatus(1l,"0");

        Assert.assertNotNull(result);
    }

    @Test
    public void testFindUserListInModule(){
        moduleService.findUserListInModule(1l);
        verify(moduleRepository, times(1)).findUserByModuleId(1l);
    }

    @Test
    public void testFindAllModuleTree(){
        List<Module> mlist = Lists.newArrayList();
        Module m1 = new Module();
        m1.setModuleName("F1");
        m1.setId(1l);
        m1.setParentId(0L);

        Module m2 = new Module();
        m2.setModuleName("C11");
        m2.setId(2l);
        m2.setParentId(1L);

        Module m3 = new Module();
        m3.setModuleName("F2");
        m3.setId(3l);
        m3.setParentId(0L);

        Module m4 = new Module();
        m4.setModuleName("C12");
        m4.setId(4l);
        m4.setParentId(1L);

        mlist.add(m1);
        mlist.add(m2);
        mlist.add(m3);
        mlist.add(m4);

        when(moduleRepository.findAll(any(org.springframework.data.domain.Sort.class))).thenReturn(mlist);

        ModuleRespVo result = moduleService.findAllModuleTree();

        Assert.assertNotNull(result);

        Assert.assertEquals(2,result.getChildren().size());
        Assert.assertFalse(result.isLeaf());

        boolean checkCh=false,checkCh2=false;

        List<ModuleRespVo> vos = result.getChildren();

        for(ModuleRespVo v:vos){
            if(v.getId()==1){
                Assert.assertEquals(2,v.getChildren().size());
                Assert.assertFalse(v.isLeaf());

                ModuleRespVo c1=v.getChildren().get(0);
                ModuleRespVo c2=v.getChildren().get(1);
                Assert.assertTrue(c1.isLeaf());
                Assert.assertTrue(c2.isLeaf());
                if((c1.getId()==2&&c2.getId()==4)||(c1.getId()==4&&c2.getId()==2)){
                    checkCh=true;
                }
            }else{
                Assert.assertEquals(0,v.getChildren().size());
                Assert.assertTrue(v.isLeaf());
                checkCh2=true;
            }
        }

        Assert.assertTrue(checkCh&&checkCh2);
    }

}
