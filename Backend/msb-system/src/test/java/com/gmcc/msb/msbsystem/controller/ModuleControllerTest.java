package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.service.ModuleService;
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

import static com.gmcc.msb.msbsystem.common.CommonConstant.ERROR_MODULE_STATUS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @program: msb-system
 * @description: 模块接口UT
 * @author: zhifanglong
 * @create: 2018-10-11 17:16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ModuleControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    @InjectMocks
    private ModuleController moduleController;
    @Mock
    private ModuleService moduleService;

    Gson gson = new Gson();

    @Before
    public void setUp(){

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllModule() throws Exception{
        RequestBuilder request = null;

        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/module/all")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result =  mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }

    @Test
    public void testModifyModuleStatus_wrong_status() throws Exception{
        RequestBuilder request = null;

        request = MockMvcRequestBuilders.request(HttpMethod.PUT,"/module/1/10")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result =  mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertNotEquals("0",resObj.getCode());
    }

    @Test
    public void testModifyModuleStatus() throws Exception{
        RequestBuilder request = null;

        request = MockMvcRequestBuilders.request(HttpMethod.PUT,"/module/1/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result =  mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }

    @Test
    public void testFindUserListInModule() throws Exception{
        RequestBuilder request = null;

        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/module/1/users")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result =  mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }

    @Test
    public void testFindModuleTree() throws Exception{
        RequestBuilder request = null;

        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/module/tree")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result =  mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());
    }

}
