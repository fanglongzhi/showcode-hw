package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.entity.role.Role;
import com.gmcc.msb.msbsystem.service.DataOrgService;
import com.gmcc.msb.msbsystem.service.RoleService;
import com.gmcc.msb.msbsystem.vo.req.role.ModifyRoleAuthParam;
import com.gmcc.msb.msbsystem.vo.req.role.ModifyUserRoleParam;
import com.google.gson.Gson;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@Import(TestConfiguration.class)
public class RoleControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    @InjectMocks
    private RoleController roleController;
    @Mock
    private RoleService roleService;
    @Mock
    private DataOrgService dataOrgService;

    Gson gson = new Gson();

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void createRoleTest_missingParam()throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        Role role = new Role();
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/role").
                content(gson.toJson(role)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0007-00007", resObj.getCode());
    }
    @Test
    public void createRoleTest_roleNameDuplicate()throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        Role role = new Role();
        role.setRoleName("test");
        when(roleService.isRoleNameUnique(role.getRoleName())).thenReturn(false);
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/role").
                content(gson.toJson(role)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0007-10025", resObj.getCode());
    }
    @Test
    public void createRoleTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        Role role = new Role();
        role.setRoleName("test");
        when(roleService.isRoleNameUnique(role.getRoleName())).thenReturn(true);
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/role").
                content(gson.toJson(role)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }
    @Test
    public void findRoleListTest()throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/roles/search").
                contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }
    @Test
    public void modifyRoleNameTest_missingParam() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        Role role = new Role();
        request = MockMvcRequestBuilders.request(HttpMethod.PUT,"/role/1").
                content(gson.toJson(role)).contentType(MediaType.APPLICATION_JSON);
        when(dataOrgService.findOrgIdsOfUser(any(String.class))).thenReturn(new ArrayList<>());
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0007-00008", resObj.getCode());
    }

    @Test
    public void modifyRoleNameTest_roleNameDuplicate()throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        Role role = new Role();
        role.setRoleName("test");
        when(roleService.isRoleNameUnique(role.getRoleName(),1l)).thenReturn(false);
        request = MockMvcRequestBuilders.request(HttpMethod.PUT,"/role/1").
                content(gson.toJson(role)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0007-00009", resObj.getCode());
    }

    @Test
    public void modifyRoleNameTest()throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        Role role = new Role();
        role.setRoleName("test");
        when(roleService.isRoleNameUnique(role.getRoleName(),1l)).thenReturn(true);
        request = MockMvcRequestBuilders.request(HttpMethod.PUT,"/role/1").
                content(gson.toJson(role)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }

    @Test
    public void deleteRoleTest_hasUser() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        when(roleService.hasUserRole(any())).thenReturn(true);
        request = MockMvcRequestBuilders.request(HttpMethod.DELETE,"/role/1").
                contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0007-10026", resObj.getCode());
    }

    @Test
    public void deleteRoleTest_hasAuth() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        when(roleService.hasAuth(any())).thenReturn(true);
        request = MockMvcRequestBuilders.request(HttpMethod.DELETE,"/role/1").
                contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0007-10027", resObj.getCode());
    }

    @Test
    public void deleteRoleTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        when(roleService.hasAuth(any())).thenReturn(false);
        when(roleService.hasUserRole(any())).thenReturn(false);
        request = MockMvcRequestBuilders.request(HttpMethod.DELETE,"/role/1").
                contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }
    @Test
    public void managerUserRoleTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        ModifyUserRoleParam param = new ModifyUserRoleParam();
        param.setAddUserIdList(Lists.newArrayList(1l));
        when(roleService.findRole(any())).thenReturn(new Role());
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/role/1/users").
                content(gson.toJson(param)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }
    @Test
    public void findAuthListTest()throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        when(roleService.findRole(any())).thenReturn(new Role());
        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/role/1/auths").
                contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }
    @Test
    public void addAuthListTest()throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        ModifyRoleAuthParam param = new ModifyRoleAuthParam();
        param.setAddAuthId(Lists.newArrayList(1l));
        when(roleService.findRole(any())).thenReturn(new Role());
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/role/1/auths").
                content(gson.toJson(param)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }

    @Test
    public void deleteRoleModuleTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.DELETE,"/role/1/auth/1").
                contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }
    @Test
    public void deleteRoleModuleBachTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        ModifyRoleAuthParam param = new ModifyRoleAuthParam();
        param.setAddAuthId(Lists.newArrayList(1l));
        when(roleService.findRole(any())).thenReturn(new Role());
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/role/1/auth/delete/batch").
                content(gson.toJson(param)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }

}
