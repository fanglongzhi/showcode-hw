package com.gmcc.msb.config.utils;

import com.gmcc.msb.config.config.MyProperties;
import com.gmcc.msb.config.service.ConfigService;
import okhttp3.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RefreshTask {

    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
    private final Integer managementPort;
    private GsonJsonParser gsonJsonParser = new GsonJsonParser();

    private ServiceInstance instance;
    private OkHttpClient client;
    private MyProperties myProperties;

    public RefreshTask(ServiceInstance instance, OkHttpClient client, MyProperties myProperties, Integer managementPort) {
        this.instance = instance;
        this.client=client;
        this.myProperties=myProperties;
        this.managementPort = managementPort;
    }


    public Map<String, String> remoteRefresh() {
        Map<String, String> result = new HashMap<>();
        String ip = ((EurekaDiscoveryClient.EurekaServiceInstance) instance).getInstanceInfo().getIPAddr();
        int port = instance.getPort();
        if (this.managementPort != null) {
            port = managementPort;
        }
        String address = ip + ":" + String.valueOf(port);
        String scheme = (instance.isSecure()) ? "https" : "http";
        String url = String.format("%s://%s:%s/%s", scheme, ip, port,
                myProperties.getRefreshPath()
        );

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        ""))
                .build();
        Response response = null;
        result.put("address", address);
        String retCode = "0";
        String retMsg="SUCCESS";
        try {
            logger.info("请求 {} 刷新配置", url);

            response = client.newCall(request).execute();
            String respStr = response.body().string();
            int code = response.code();
            if (code != 200) {
                logger.error("刷新配置失败，{}, {}", code, respStr);
                retCode=String.valueOf(code);
                retMsg=respStr;
            } else {
                if (respStr.indexOf("{") > -1) {
                    try {
                        Map resultMap = gsonJsonParser.parseMap(respStr);

                        Object remoteCode = resultMap.get("code");
                        if (remoteCode != null && !("0".equals(remoteCode))) {
                            logger.error("刷新配置失败，{}, {}", code, respStr);
                            retCode=String.valueOf(remoteCode);
                            retMsg=(String)resultMap.get("message");
                        }
                    } catch (Exception e) {
                        logger.error("错误：",e);
                       retCode=e.getMessage();
                       retMsg=e.getMessage();
                    }

                }else{
                    retCode="0";
                }

            }
        } catch (IOException e) {
            logger.error("刷新配置失败，{}", ExceptionUtils.getMessage(e));
            retCode=e.getMessage();
            retMsg=e.getMessage();
        }

        if("0".equals(retCode)){
            logger.info("请求 {} 刷新配置成功", url);
        }

        result.put("code",retCode);
        result.put("message",retMsg);
        return result;

    }
}
