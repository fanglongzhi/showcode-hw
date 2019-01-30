package com.msb.join.demo.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 向注册中心定时上送心跳信息
 * 当三次心跳失败后，重新发送注册请求，直到注册成功，继续下一轮心跳上送
 */
public class HeartTimer {
    public static final Logger log = LoggerFactory.getLogger(Heartbeat.class);
    private ScheduledExecutorService timer;

    public HeartTimer(String registerUrl, String appId, String instanceId, Register register) {
        timer = new ScheduledThreadPoolExecutor(1);
        timer.scheduleWithFixedDelay(new Heartbeat(registerUrl, appId, instanceId, register), 30, 30, TimeUnit.SECONDS);
    }
}

class Heartbeat extends TimerTask {
    private String registerUrl;
    private String appId;
    private String instanceId;
    private Register register;
    int failCount = 0;

    OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .connectTimeout(2000, TimeUnit.MILLISECONDS)
            .readTimeout(4000, TimeUnit.MILLISECONDS)
            .build();

    public Heartbeat(String registerUrl, String appId, String instanceId, Register register) {
        this.registerUrl = registerUrl;
        this.appId = appId;
        this.instanceId = instanceId;
        this.register = register;
    }

    @Override
    public void run() {
        String url = registerUrl + "/apps/" + appId + "/" + instanceId;
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"), ""
                ))
                .build();
        Response response = null;

        int code = 404;
        try {
            if(failCount<3) {
                response = client.newCall(request).execute();
                code = response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(failCount<3) {
            HeartTimer.log.info("Heartbeat:" + new Date() + "::::code:" + code);
            if (code != 200) {
                failCount++;
                HeartTimer.log.error("第" + failCount + "次心跳链接失败");
            }
        }

        if (failCount >= 3) {
            HeartTimer.log.info("重新链接注册中心");
            try {
                register.registerAgain();
                failCount = 0;
            }catch (Exception e){
                HeartTimer.log.error("重连注册中心失败,",e);
            }
        }
    }
}
