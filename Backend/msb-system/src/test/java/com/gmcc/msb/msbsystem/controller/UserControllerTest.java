package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.msbsystem.common.UserStatus;
import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.service.UserService;
import com.gmcc.msb.msbsystem.vo.req.user.CreateUserParam;
import com.gmcc.msb.msbsystem.vo.req.user.QueryUserParam;
import com.google.gson.Gson;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;

    Gson gson = new Gson();

    @Before
    public void setUp(){

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUserTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        CreateUserParam user = new CreateUserParam();
        user.setOperatorId(1l);
        user.setPersonCardNo("11");
        user.setMobile("11");
        user.setLoginId("11");
        user.setEmail("test@email.com");
        user.setUserId("1l");
        user.setOrgId(1l);
        user.setName("test");

        when(userService.saveUser(any(User.class))).thenReturn(Result.success());
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/user").
                content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }

    @Test
    public void createUserTest_missparam() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        CreateUserParam user = new CreateUserParam();
        user.setOperatorId(1l);
        user.setPersonCardNo("11");
        user.setMobile("11");
        user.setLoginId("11");
        user.setEmail("com");
        user.setUserId("1l");
        user.setOrgId(1l);
        user.setName("test");

        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/user").
                content(gson.toJson(user)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0007-00005", resObj.getCode());
    }

    @Test
    public void findUserTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;

        when(userService.findOneUser(any(Long.class))).thenReturn(Result.success());
        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/user/1")
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }
    @Test
    public void fineUserListTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        when(userService.findUserListWithDataOrgInfo(any())).thenReturn(Result.success());

        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/users/search")
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());

    }
    @Test
    public void modifyUserTest() throws Exception{
        RequestBuilder request = null;
        MvcResult result = null;
        QueryUserParam.ModifyUserParam param = new QueryUserParam.ModifyUserParam();
        param.setStatus(UserStatus.VALID);

        when(userService.modifyUserStatus(any())).thenReturn(Result.success());

        request = MockMvcRequestBuilders.request(HttpMethod.PUT,"/user/1").content(gson.toJson(param))
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }
    @Test
    public void testModifyUser_Status(){
        QueryUserParam.ModifyUserParam param = new QueryUserParam.ModifyUserParam();
        param.setStatus(UserStatus.VALID);
        Result mockResult = Result.success();
        when(userService.modifyUserStatus(param)).thenReturn(mockResult);
        Result result =userController.modifyUser(1L,param);

        verify(userService, times(1)).modifyUserStatus(param);
        verify(userService, times(0)).modifyUserLock(param);

        Assert.assertEquals(Long.valueOf(1L),param.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals("0", result.getCode());


    }
    @Test
    public void testModifyUser_Lock(){
        QueryUserParam.ModifyUserParam param = new QueryUserParam.ModifyUserParam();
        param.setIsLock(true);
        Result mockResult = Result.success();
        when(userService.modifyUserLock(param)).thenReturn(mockResult);
        Result result =userController.modifyUser(1L,param);

        verify(userService, times(0)).modifyUserStatus(param);
        verify(userService, times(1)).modifyUserLock(param);

        Assert.assertEquals(Long.valueOf(1L),param.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals("0", result.getCode());

    }
}
