package com.gmcc.msb.config.controller;

import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.config.MsbTestConfiguration;
import com.gmcc.msb.config.common.Result;
import com.gmcc.msb.config.entity.Config;
import com.gmcc.msb.config.service.ConfigService;
import com.gmcc.msb.config.service.SignKeyRefreshService;
import com.gmcc.msb.config.vo.ConfigVo;
import com.gmcc.msb.config.vo.RefreshResultVo;
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

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(MsbTestConfiguration.class)
public class ConfigControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    @InjectMocks
    private ConfigController configController;

    @Mock
    private ConfigService configService;
    @Mock
    private SignKeyRefreshService signKeyRefreshService;

    @Mock
    private MsbProperties msbProperties;

    Gson gson = new Gson();

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void createServiceConfigTest_missingParam() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        ConfigVo configVo = new ConfigVo();
        configVo.setApplication("MSB-TEST");

        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/serviceConfig").
                                                                                           content(gson.toJson(configVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);

        Assert.assertEquals("0003-00001", resObj.getCode());

    }

    @Test
    public void createServiceConfigTest_beyondLong() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        ConfigVo configVo = new ConfigVo();
        configVo.setApplication(padString(51));
        configVo.setProfile(padString(21));
        configVo.setPropertyKey(padString(1024 + 1));
        configVo.setPropertyValue(padString(2048 + 1));
        configVo.setLabel(padString(21));

        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/serviceConfig").
                                                                                           content(gson.toJson(configVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);

        Assert.assertEquals("0003-00001", resObj.getCode());

    }

    private String padString(int length) {
        StringBuilder sb = new StringBuilder();
        while (length-- > 0) {
            sb.append("a");
        }
        return sb.toString();
    }

    @Test
    public void createServiceConfigTest_exist() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        ConfigVo configVo = new ConfigVo();
        configVo.setApplication("MSB-TEST");
        configVo.setProfile("TEST");
        configVo.setPropertyKey("testKey");
        configVo.setPropertyValue("testValue");

        when(configService.countConfig(any())).thenReturn(1);
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/serviceConfig").
                                                                                           content(gson.toJson(configVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);

        Assert.assertEquals("0003-10001", resObj.getCode());

    }

    @Test
    public void createServiceConfigTest_success() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        ConfigVo configVo = new ConfigVo();
        configVo.setApplication("MSB-TEST");
        configVo.setProfile("TEST");
        configVo.setPropertyKey("testKey");
        configVo.setPropertyValue("testValue");

        when(configService.countConfig(any())).thenReturn(0);
        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/serviceConfig").
                                                                                           content(gson.toJson(configVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);

        Assert.assertEquals("0", resObj.getCode());

    }

    @Test
    public void modifyServiceConfigTest() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        ConfigVo configVo = new ConfigVo();
        configVo.setApplication("MSB-TEST");
        configVo.setProfile("TEST");
        configVo.setPropertyKey("testKey");
        configVo.setPropertyValue("testValue");


        request = MockMvcRequestBuilders.request(HttpMethod.PUT, "/serviceConfig/1").
                                                                                            content(gson.toJson(configVo)).contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);

        Assert.assertEquals("0", resObj.getCode());

    }

    @Test
    public void findServiceConfigForApplicationTest() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        Config config = new Config();
        config.setApplication("msb-test");
        config.setPropertyKey("testKey");
        config.setPropertyValue("testValue");
        List<Config> configList = Lists.newArrayList();
        configList.add(config);
        when(configService.findByApplication("msb-test")).thenReturn(configList);
        request = MockMvcRequestBuilders.request(HttpMethod.GET, "/application/msb-test/serviceConfig");

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);
        Assert.assertEquals("0", resObj.getCode());
        Assert.assertNotNull(resObj.getContent());

        List list = (List) resObj.getContent();
        Assert.assertEquals(1, list.size());
        Config conRes = gson.fromJson(list.get(0).toString(), Config.class);
        Assert.assertEquals(config.getApplication(), conRes.getApplication());

    }

    @Test
    public void findServiceConfigForApplicationAndProfileTest() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        Config config = new Config();
        config.setApplication("msb-test");
        config.setProfile("test");
        config.setPropertyKey("testKey");
        config.setPropertyValue("testValue");
        List<Config> configList = Lists.newArrayList();
        configList.add(config);
        when(configService.findByApplicationAndProfile("msb-test", "test")).thenReturn(configList);
        request = MockMvcRequestBuilders.request(HttpMethod.GET, "/application/msb-test/test/serviceConfig");

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);
        Assert.assertEquals("0", resObj.getCode());
        Assert.assertNotNull(resObj.getContent());

        List list = (List) resObj.getContent();
        Assert.assertEquals(1, list.size());
        Config conRes = gson.fromJson(list.get(0).toString(), Config.class);
        Assert.assertEquals(config.getApplication(), conRes.getApplication());

    }


    @Test
    public void deleteServiceConfig() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        request = MockMvcRequestBuilders.request(HttpMethod.DELETE, "/serviceConfig/1");

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);
        Assert.assertEquals("0", resObj.getCode());

    }

    @Test
    public void refreshServiceConfig() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;
        when(configService.refreshConfig("msb-test")).thenReturn(new RefreshResultVo());

        request = MockMvcRequestBuilders.request(HttpMethod.GET, "/application/msb-test/serviceConfig");

        request = MockMvcRequestBuilders.request(HttpMethod.POST, "/msb-test/serviceConfig/refresh");

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);
        Assert.assertEquals("0", resObj.getCode());

    }

    @Test
    public void findSystemProfile() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;

        request = MockMvcRequestBuilders.request(HttpMethod.GET, "/serviceConfig/profile");

        result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(result);
        String res = result.getResponse().getContentAsString();
        Result resObj = gson.fromJson(res, Result.class);
        Assert.assertEquals("0", resObj.getCode());
        Assert.assertEquals("utest", resObj.getContent());

    }
}
