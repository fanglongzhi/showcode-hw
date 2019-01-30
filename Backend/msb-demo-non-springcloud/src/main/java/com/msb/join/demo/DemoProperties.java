package com.msb.join.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @program:
 * @description: 属性类
 * @author: zhifanglong
 * @create: 2018-12-18 15:59
 */
@Component
public class DemoProperties {
    private List<String> skipSignCheckUri = Arrays.asList("/error","/refresh");
    @Value("${spring.application.name:default}")
    private String applicationName;
    @Value("${eureka.client.service-url.defaultZone:}")
    private String registerCenterUrl;
    @Value("${server-port:8080}")
    private String serverPort;
    @Value("${config-center-url:}")
    private String configUrl;
    @Value("${spring.profiles.active:local}")
    private String profile;
    @Value("${config-userName:}")
    private String configUserName;
    @Value("${config-password:}")
    private String configPassword;
    @Value("${helloMsg:hello}")
    private String helloMsg;
    @Value("${msb.signKey}")
    private String msbSignKey;
    @Value("${msb.serviceCode}")
    private String serviceCode;
    @Value("${spring.zipkin.base-url:}")
    private String zipkinUrl;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegisterCenterUrl() {
        return registerCenterUrl;
    }

    public void setRegisterCenterUrl(String registerCenterUrl) {
        this.registerCenterUrl = registerCenterUrl;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getConfigUserName() {
        return configUserName;
    }

    public void setConfigUserName(String configUserName) {
        this.configUserName = configUserName;
    }

    public String getConfigPassword() {
        return configPassword;
    }

    public void setConfigPassword(String configPassword) {
        this.configPassword = configPassword;
    }

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getHelloMsg() {
        return helloMsg;
    }

    public void setHelloMsg(String helloMsg) {
        this.helloMsg = helloMsg;
    }

    public String getMsbSignKey() {
        return msbSignKey;
    }

    public void setMsbSignKey(String msbSignKey) {
        this.msbSignKey = msbSignKey;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public List<String> getSkipSignCheckUri() {
        return skipSignCheckUri;
    }

    public void setSkipSignCheckUri(List<String> skipSignCheckUri) {
        this.skipSignCheckUri = skipSignCheckUri;
    }

    public String getZipkinUrl() {
        return zipkinUrl;
    }

    public void setZipkinUrl(String zipkinUrl) {
        this.zipkinUrl = zipkinUrl;
    }
}
