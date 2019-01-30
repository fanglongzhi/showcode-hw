package com.gmcc.msb.demo.provider.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class HelloControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @InjectMocks
    private HelloController helloController;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSayHello() throws Exception {
        String name = "William";
        RequestBuilder request = MockMvcRequestBuilders.request(HttpMethod.GET,"/say_hello/" + name);
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        Assert.assertEquals("Hello, " + name, result.getResponse().getContentAsString());
    }
}
