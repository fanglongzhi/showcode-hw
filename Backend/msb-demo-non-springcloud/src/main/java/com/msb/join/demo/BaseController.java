package com.msb.join.demo;

import com.msb.join.demo.utils.Config;
import com.msb.join.demo.utils.SignCheckFilter;
import com.msb.join.demo.utils.UserContextHolder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program:
 * @description: 基本URL
 * @author: zhifanglong
 * @create: 2018-12-19 10:36
 */
@RestController
public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    private Config config;
    @Autowired
    private DemoProperties demoProperties;
    //@Autowired
    //private CloseableHttpClient httpClient;

    /**
     * 调用其他服务，请使用经过配置的CloseableHttpClient,这样可以向zipkin服务器报告调用链数据
     */
   /* @GetMapping("/z")
    public String service() throws Exception {
        HttpGet get = new HttpGet("http://localhost:10001/re");
        CloseableHttpResponse response = httpClient.execute(get);
        return EntityUtils.toString(response.getEntity(), "utf-8");
    }*/

    /**
     * 服务配置动态刷新
     *
     * @return
     */
    @PostMapping("/refresh")
    public Map refresh() {
        String code = "0";
        String message = "success";
        try {
            config.fetchConfig();
        } catch (Exception e) {
            code = e.getMessage();
            message = e.getMessage();
        }

        Map<String, String> result = new HashMap<>();
        result.put("message", message);
        result.put("code", code);
        return result;
    }

    @GetMapping("/info")
    public Map info(){
        Map<String,String> info = new HashMap<>();
        info.put("service-id","msb-demo-non-springcloud");
        info.put("description","This is a demo based on spring boot");
        return info;
    }
    @GetMapping("/health")
    public String health(){
        return "SUCCESS";
    }
    /**
     * 匿名接口
     * @return
     */
    @GetMapping("/hello")
    public String hello(){

        return demoProperties.getHelloMsg();
    }

    /**
     * 非匿名接口
     * @return
     */
    @GetMapping("/hello/info")
    public String helloInfo(){
        if(UserContextHolder.getContext().getUserId()==null){
            return "401::未授权";
        }
        return UserContextHolder.getContext().getUserId()+":"+demoProperties.getHelloMsg();
    }

}
