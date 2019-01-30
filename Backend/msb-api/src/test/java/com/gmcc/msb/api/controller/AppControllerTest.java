package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.common.CommonConstant;
import com.gmcc.msb.MsbTestConfiguration;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.service.AppService;
import com.gmcc.msb.api.vo.request.AppRequest;
import com.gmcc.msb.api.vo.request.ModifyAppRequest;
import com.gmcc.msb.common.vo.Response;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(MsbTestConfiguration.class)
public class AppControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    @InjectMocks
    private AppController appController;

    @Mock
    private AppService appService;

    private Gson gson = new Gson();

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateApplication() throws Exception {
        AppRequest appRequest = new AppRequest();
        appRequest.setAppName("testAppName");
        appRequest.setCompany("company");
        appRequest.setLinkEmail("email");
        appRequest.setLinkMan("man");
        appRequest.setLinkTel("tel");
        appRequest.setDescription("desc");

        when(appService.countAppName(appRequest.getAppName())).thenReturn(0);
        when(appService.createApplication(any())).thenReturn(new App());

        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/app").
                                                                                 content(gson.toJson(appRequest)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals("0", resObj.getCode());
        App app = gson.fromJson(resObj.getContent().toString(), App.class);

        Assert.assertNotNull(app);
        //assertThat(app).extracting("appSecret").isNotEmpty();
    }

    @Test
    public void testCreateApplication_valid_error() throws Exception {
        AppRequest appRequest = new AppRequest();
        appRequest.setCompany("company");
        appRequest.setLinkEmail("email");
        appRequest.setLinkMan("man");
        appRequest.setLinkTel("tel");
        appRequest.setDescription("desc");

        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/app").
                                                                                 content(gson.toJson(appRequest)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals(CommonConstant.ERROR_REQUEST_DATA_CODE, resObj.getCode());
    }

    @Test
    public void testCreateApplication_duplicate() throws Exception {
        AppRequest appRequest = new AppRequest();
        appRequest.setAppName("testName");
        appRequest.setCompany("company");
        appRequest.setLinkEmail("email");
        appRequest.setLinkMan("man");
        appRequest.setLinkTel("tel");
        appRequest.setDescription("desc");

        when(appService.countAppName(appRequest.getAppName())).thenReturn(1);

        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/app").
                                                                                 content(gson.toJson(appRequest)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals(CommonConstant.ERROR_APP_EXIST_CODE, resObj.getCode());
    }

    @Test
    public void testGetAppList() throws Exception {

        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.GET, "/app/list/all").contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals("0", resObj.getCode());
    }

    @Test
    public void testGetApp() throws Exception {
        App app = new App();
        app.setAppName("testApp");

        when(appService.findOneApp(1)).thenReturn(app);
        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.GET, "/app/1").contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals("0", resObj.getCode());

        App retApp = gson.fromJson(resObj.getContent().toString(), App.class);

        assertThat(retApp).isNotNull().extracting("appName").contains(app.getAppName());
    }

    @Test
    public void testDeleteApp() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.DELETE, "/app/1").contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals("0", resObj.getCode());

    }

    @Test
    public void testApplyApp() throws Exception {
        App app = new App();
        app.setAppName("testApp");

        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/app/1/apply").content(gson.toJson(new App())).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals("0", resObj.getCode());

    }

    @Test
    public void testFindApi() throws Exception {

        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.GET, "/app/1/apis").contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals("0", resObj.getCode());

    }

    @Test
    public void testModifyApp_valid_error() throws Exception {
        ModifyAppRequest appRequest = new ModifyAppRequest();
        appRequest.setCompany("company");
        appRequest.setLinkEmail("email");
        appRequest.setLinkMan("man");
        appRequest.setLinkTel("tel");
        appRequest.setDescription("desc");

        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/app/1").
                                                                                   content(gson.toJson(appRequest)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals(CommonConstant.ERROR_REQUEST_DATA_CODE, resObj.getCode());
    }

    @Test
    public void testModifyApp_name_duplicate() throws Exception {
        ModifyAppRequest appRequest = new ModifyAppRequest();
        appRequest.setCompany("company");
        appRequest.setLinkEmail("email");
        appRequest.setLinkMan("man");
        appRequest.setLinkTel("tel");
        appRequest.setDescription("desc");
        appRequest.setAppName("testName");

        when(appService.countAppName(appRequest.getAppName(), 1)).thenReturn(1);
        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/app/1").
                                                                                   content(gson.toJson(appRequest)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals(CommonConstant.ERROR_APP_EXIST_CODE, resObj.getCode());
    }

    @Test
    public void testModifyApp() throws Exception {
        ModifyAppRequest appRequest = new ModifyAppRequest();
        appRequest.setCompany("company");
        appRequest.setLinkEmail("email");
        appRequest.setLinkMan("man");
        appRequest.setLinkTel("tel");
        appRequest.setDescription("desc");
        appRequest.setAppName("testName");

        when(appService.countAppName(appRequest.getAppName(), 1)).thenReturn(0);
        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/app/1").
                                                                                   content(gson.toJson(appRequest)).contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Response resObj = gson.fromJson(res, Response.class);

        Assert.assertEquals("0", resObj.getCode());
    }


}
