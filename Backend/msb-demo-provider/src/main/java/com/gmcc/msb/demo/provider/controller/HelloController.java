package com.gmcc.msb.demo.provider.controller;

import com.gmcc.msb.demo.provider.filter.FetchHeaderFilter;
import com.gmcc.msb.demo.provider.property.MsbProperties;
import com.gmcc.msb.demo.provider.util.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello Controller
 * @author sirui.lin
 */
@RestController
public class HelloController {
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    private MsbProperties msbProperties;
    /**
     * 可以匿名访问的示例接口
     * @param name
     * @return
     */
    @GetMapping("/say_hello/{name}")
    public String sayHello(@PathVariable String name) {
        String message = String.format("Hello, %s", name);
        return message;
    }

    /**
     * 不可以匿名访问的接口示例
     * @param name
     * @return
     */
    @GetMapping("/say_hello/{name}/info")
    public String sayHelloPersonal(@PathVariable String name){
       String userId = UserContextHolder.getContext().getUserId();
       log.info("USERID:::CONTROLLER:"+userId);
        if (userId == null || "".equals(userId)) {
            return msbProperties.getServiceCode() +"-11002:用户不存在";
        }

        return String.format("Hello, %s,你的ID是：%s,你是授权用户", name,userId);

    }
}
