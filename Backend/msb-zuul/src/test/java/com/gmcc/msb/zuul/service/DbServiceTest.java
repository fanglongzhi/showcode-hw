package com.gmcc.msb.zuul.service;

import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbServiceTest {


    @InjectMocks
    DbService dbService;

    @Mock
    JdbcTemplate jdbcTemplate;

    @Mock
    private MsbZuulProperties msbZuulProperties;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testgetRoutes(){

    }
}
