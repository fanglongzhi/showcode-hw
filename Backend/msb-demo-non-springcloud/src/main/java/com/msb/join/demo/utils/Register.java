package com.msb.join.demo.utils;

import com.google.gson.Gson;
import com.msb.join.demo.DemoProperties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @program:
 * @description: 服务注册对象
 * @author: zhifanglong
 * @create: 2018-11-14 17:14
 */
@Component
public class Register {
    public static final Logger log = LoggerFactory.getLogger(Register.class);
    @Autowired
    private DemoProperties demoProperties;

    private String IP;
    private String hostName;

    public Register() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            IP = addr.getHostAddress().toString(); //获取本机ip
            hostName = addr.getHostName().toString(); //获取本机计算机名称
        } catch (Exception e) {
            log.error("error is ", e);
        }
    }

    private OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .connectTimeout(2000, TimeUnit.MILLISECONDS)
            .readTimeout(4000, TimeUnit.MILLISECONDS)
            .build();

    Gson gson = new Gson();

    public Object register() throws Exception {
        String serverPort = demoProperties.getServerPort();
        String registerCenterUrl = demoProperties.getRegisterCenterUrl();
        String applicationName = demoProperties.getApplicationName();
        String myAddr = "http://" + IP + ":" + serverPort;
        String url = registerCenterUrl + "/apps/" + applicationName;
        String appId = applicationName;
        String instanceId = appId + ":" + hostName + ":" + serverPort;
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> instanceInfo = new HashMap<>();
        Map<String, Object> port = new HashMap<>();
        Map<String, Object> dataCenterInfo = new HashMap<>();

        port.put("$", serverPort);
        port.put("@enabled", true);

        Map<String, Object> port2 = new HashMap<>();

        port2.put("$", "443");
        port2.put("@enabled", false);

        dataCenterInfo.put("@class", "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo");
        dataCenterInfo.put("name", "MyOwn");

        instanceInfo.put("hostName", IP);
        instanceInfo.put("instanceId", instanceId);
        instanceInfo.put("app", applicationName);
        instanceInfo.put("vipAddress", applicationName);
        instanceInfo.put("secureVipAddress", applicationName);
        instanceInfo.put("statusPageUrl", myAddr + "/info");
        instanceInfo.put("healthCheckUrl", myAddr + "/health");
        instanceInfo.put("ipAddr", IP);
        instanceInfo.put("homePageUrl", myAddr + "/info");
        instanceInfo.put("port", port);
        instanceInfo.put("securePort",port2);
        instanceInfo.put("dataCenterInfo", dataCenterInfo);
        instanceInfo.put("isCoordinatingDiscoveryServer",false);

        body.put("instance", instanceInfo);

        String json = gson.toJson(body);

        System.out.println("======send:" + json);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        json))
                .build();
        Response response = null;

        response = client.newCall(request).execute();
        int code = response.code();

        String respStr = response.body().string();
        System.out.println("response======" + respStr);

        if (code == 204) {
            new HeartTimer(registerCenterUrl, appId, instanceId, this);
        }


        return "";
    }

    public Object registerAgain() throws Exception {
        String serverPort = demoProperties.getServerPort();
        String registerCenterUrl = demoProperties.getRegisterCenterUrl();
        String applicationName = demoProperties.getApplicationName();
        String myAddr = "http://" + IP + ":" + serverPort;
        String url = registerCenterUrl + "/apps/" + applicationName;
        String appId = applicationName;
        String instanceId = appId + ":" + hostName + ":" + serverPort;
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> instanceInfo = new HashMap<>();
        Map<String, Object> port = new HashMap<>();
        Map<String, Object> dataCenterInfo = new HashMap<>();

        port.put("$", serverPort);
        port.put("@enabled", true);

        Map<String, Object> port2 = new HashMap<>();

        port2.put("$", "443");
        port2.put("@enabled", false);

        dataCenterInfo.put("@class", "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo");
        dataCenterInfo.put("name", "MyOwn");

        instanceInfo.put("hostName", IP);
        instanceInfo.put("instanceId", instanceId);
        instanceInfo.put("app", applicationName);
        instanceInfo.put("vipAddress", applicationName);
        instanceInfo.put("secureVipAddress", applicationName);
        instanceInfo.put("statusPageUrl", myAddr + "/info");
        instanceInfo.put("healthCheckUrl", myAddr + "/health");
        instanceInfo.put("ipAddr", IP);
        instanceInfo.put("homePageUrl", myAddr + "/info");
        instanceInfo.put("port", port);
        instanceInfo.put("securePort",port2);
        instanceInfo.put("dataCenterInfo", dataCenterInfo);
        instanceInfo.put("isCoordinatingDiscoveryServer",false);


        body.put("instance", instanceInfo);

        String json = gson.toJson(body);

        System.out.println("======send:" + json);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        json))
                .build();
        Response response = null;
        response = client.newCall(request).execute();
        int code = response.code();

        String respStr = response.body().string();
        System.out.println("response======" + respStr);

        return "";
    }
}
