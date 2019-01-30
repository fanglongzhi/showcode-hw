package com.msb.join.demo.utils;

import com.msb.join.demo.DemoProperties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @program:
 * @description: 获取配置信息
 * 1.获取配置信息后，可以更新本地内存中的相关变量的值，实现动态刷新
 * 2.如果，属性值以{cipher}开头，则需要先解密，后使用
 * @author: zhifanglong
 * @create: 2018-12-18 15:44
 */
@Component
public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static final String REFRESH_EUREKA_URL = "eureka.client.service-url.defaultZone";
    private static final String REFRESH_HELLO_MSB="helloMsg";
    private static final String MSB_SIGN_KEY="msb.signKey";
    private static final String ZIP_KEY_SERVER="spring.zipkin.base-url";
    @Autowired
    private DemoProperties demoProperties;

    private OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .connectTimeout(2000, TimeUnit.MILLISECONDS)
            .readTimeout(4000, TimeUnit.MILLISECONDS)
            .build();

    public void fetchConfig() throws Exception {
        Properties properties = fetchProperties();
        if(properties!=null) {
            for (Map.Entry<Object, Object> en : properties.entrySet()) {
                String value=(String)en.getValue();
                if(value.startsWith("{cipher}")){
                    value = EncipherTool.decode(value.substring(8));
                }
                if (en.getKey().equals(REFRESH_EUREKA_URL)) {
                    demoProperties.setRegisterCenterUrl(value);
                } else if (en.getKey().equals(REFRESH_HELLO_MSB)) {
                    demoProperties.setHelloMsg(value);
                }else if(en.getKey().equals(MSB_SIGN_KEY)){
                    demoProperties.setMsbSignKey(value);
                }else if(en.getKey().equals(ZIP_KEY_SERVER)){
                    demoProperties.setZipkinUrl(value);
                }
            }
        }else{
            throw new Exception("CODE:401;配置服务器鉴权失败！");
        }
    }

    private Properties fetchProperties() {
        //String url="http://localhost:8888/msb-marvel-local.properties";
        String configUrl = demoProperties.getConfigUrl();
        String serviceId = demoProperties.getApplicationName();
        String profile = demoProperties.getProfile();
        String userName = demoProperties.getConfigUserName();
        String password = demoProperties.getConfigPassword();

        String baseStr = new String(Base64.getEncoder().encode((userName + ":" + password).getBytes()));
        String certificate = "Basic " + baseStr;
        String url = configUrl + "/" + serviceId
                + "-" + profile + ".properties";
        url = url.replaceAll("/+","/");
        Request request = new Request.Builder()
                .header("authorization", certificate)
                .url(url)
                .get()
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            int code = response.code();
            String message = response.body().string();
            log.info("Fetch :::code:" + code);
            log.info("env is " + message);
            Properties properties = new Properties();
            InputStream in = new ByteArrayInputStream(message.getBytes());
            if (code != 200) {
                log.error("获取配置信息失败;CODE::" + code);
                return null;
            }
            properties.load(in);

            return properties;
        } catch (Exception e) {
            log.error("获取配置信息失败", e);
        }

        return null;
    }


}
