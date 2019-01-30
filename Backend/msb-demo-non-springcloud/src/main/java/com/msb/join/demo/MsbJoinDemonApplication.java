package com.msb.join.demo;

import com.msb.join.demo.utils.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class MsbJoinDemonApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsbJoinDemonApplication.class, args);
    }

}
