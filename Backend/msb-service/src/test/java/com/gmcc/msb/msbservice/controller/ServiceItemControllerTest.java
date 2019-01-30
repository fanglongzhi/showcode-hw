package com.gmcc.msb.msbservice.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.service.client.MsbApiClient;
import com.gmcc.msb.common.service.client.MsbConfigClient;
import com.gmcc.msb.common.service.client.MsbRouteClient;
import com.gmcc.msb.msbservice.common.resp.Result;
import com.gmcc.msb.msbservice.service.ServiceItemService;
import com.gmcc.msb.msbservice.vo.ServiceItemVo;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ServiceItemControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    @InjectMocks
    private ServiceItemController serviceItemController;

    @Mock
    private ServiceItemService serviceItemService;

    @Mock
    private MsbApiClient msbApiClient;

    @Mock
    private MsbConfigClient msbConfigClient;

    @Mock
    private MsbRouteClient msbRouteClient;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private Gson gson = new Gson();
    @Before
    public void setUp(){

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void findServiceItemAllTest() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/serviceItem/all");
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
    }

    @Test
    public void findServiceStatusTest() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        request = MockMvcRequestBuilders.request(HttpMethod.GET,"/serviceItem/1/status");
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);


    }

    @Test
    public void syncServiceListTest() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/serviceItem/sync");
        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
    }

    @Test
    public void createServiceItemTest_success() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        ServiceItemVo itemVo = new ServiceItemVo();
        itemVo.setServiceName("test");
        itemVo.setServiceId("test");
        itemVo.setServiceCode("9999");
        when(serviceItemService.checkServiceItemDuplicate(any(),any())).thenReturn(false);
        when(serviceItemService.checkServiceCodeDuplicate(any())).thenReturn(false);
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/serviceItem").
                content(gson.toJson(itemVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0",resObj.getCode());

    }

    @Test
    public void createServiceItemTest_beyondLong() throws Exception {

        expectedEx.expect(Exception.class);
        RequestBuilder request = null;
        MvcResult result = null;

        ServiceItemVo itemVo = new ServiceItemVo();
        itemVo.setServiceName(padString(51));
        itemVo.setServiceId(padString(51));
        itemVo.setServiceCode("9999");
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/serviceItem").
                content(gson.toJson(itemVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0006-00001",resObj.getCode());

    }

    @Test
    public void createServiceItemTest_missingParam() throws Exception {
        expectedEx.expect(Exception.class);
        RequestBuilder request = null;
        MvcResult result = null;

        ServiceItemVo itemVo = new ServiceItemVo();

        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/serviceItem").
                content(gson.toJson(itemVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0006-00001",resObj.getCode());

    }

    @Test
    public void createServiceItemTest_Duplicate() throws Exception {
        expectedEx.expect(Exception.class);
        RequestBuilder request = null;
        MvcResult result = null;

        ServiceItemVo itemVo = new ServiceItemVo();
        itemVo.setServiceName("test");
        itemVo.setServiceId("test");
        itemVo.setServiceCode("9999");
        when(serviceItemService.checkServiceItemDuplicate(any(),any())).thenReturn(true);
        when(serviceItemService.checkServiceCodeDuplicate(any())).thenReturn(false);
        request = MockMvcRequestBuilders.request(HttpMethod.POST,"/serviceItem").
                content(gson.toJson(itemVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj =gson.fromJson(res, Result.class);

        Assert.assertEquals("0006-00002",resObj.getCode());

    }

    private String padString(int length){
        StringBuilder sb = new StringBuilder();
        while(length-->0){
            sb.append("a");
        }
        return sb.toString();
    }

}
