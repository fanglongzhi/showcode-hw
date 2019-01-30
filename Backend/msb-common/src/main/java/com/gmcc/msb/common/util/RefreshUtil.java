package com.gmcc.msb.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.vo.Constant;
import okhttp3.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 调用微服务的刷新接口
 *
 * @author Yuan Chunhai
 * @Date 11/7/2018-9:50 AM
 */
public class RefreshUtil {

    private static final Logger logger = LoggerFactory.getLogger(RefreshUtil.class);
    private final DiscoveryClient discoveryClient;
    private final String serviceId;
    private final MsbProperties msbProperties;


    private long connectionTime = 3000;
    private long readTime = 3000;

    public RefreshUtil(DiscoveryClient discoveryClient, String serviceId, MsbProperties msbProperties) {
        this.discoveryClient = discoveryClient;
        this.serviceId = serviceId;
        this.msbProperties = msbProperties;
    }

    public RefreshUtil(DiscoveryClient discoveryClient, String serviceId, MsbProperties msbProperties, long connectionTime, long readTime) {
        this.discoveryClient = discoveryClient;
        this.serviceId = serviceId;
        this.msbProperties = msbProperties;
        this.connectionTime = connectionTime;
        this.readTime = readTime;
    }


    /**
     * 刷新所有节点
     *
     * @param path                       路径
     * @param jsonBodyStr                post请求体，json字符串
     * @param msg                        提示信息
     * @param requestFailErrorCode       请求失败错误码
     * @param requestReturnFailErrorCode 请求返回200后，返回错误码不为0时的错误码
     * @param networkFailErrorCode       网络异常错误码
     * @param noInstantErrorCode         没有实例错误码
     */
    public void refresh(String path, String jsonBodyStr, String msg, String requestFailErrorCode, String requestReturnFailErrorCode, String networkFailErrorCode, String noInstantErrorCode) {

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (instances != null && !instances.isEmpty()) {
            logger.info("需要同步{}个节点", instances.size());
            for (ServiceInstance instance : instances) {

                OkHttpClient client = new OkHttpClient()
                                              .newBuilder()
                                              .connectTimeout(connectionTime, TimeUnit.MILLISECONDS)
                                              .readTimeout(readTime, TimeUnit.MILLISECONDS)
                                              .build();

                //
                String scheme = (instance.isSecure()) ? "https" : "http";
                String url = String.format("%s://%s:%s%s", scheme,
                        ((EurekaDiscoveryClient.EurekaServiceInstance) instance).getInstanceInfo().getIPAddr(),
                        instance.getPort(),
                        path
                );

                String sign = SignUtils.signRequest(path, "", msbProperties.getCommonSignKey());
                Request request = new Request.Builder()
                                          .url(url)
                                          .header(Constant.MSB_INNER_CALL, "true")
                                          .header(Constant.SIGN, sign)
                                          .post(RequestBody.create(
                                                  MediaType.parse("application/json; charset=utf-8"),
                                                  jsonBodyStr))
                                          .build();
                Response response = null;
                try {
                    logger.info("请求 {} {}", url, msg);
                    response = client.newCall(request).execute();
                    int code = response.code();
                    if (code != 200) {
                        String respStr = response.body().string();
                        logger.error("{} 失败，{}, {}", msg, code, respStr);
                        throw new MsbException(requestFailErrorCode, String.valueOf(code));
                    } else {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String respStr = response.body().string();
                        com.gmcc.msb.common.vo.Response returnVo = objectMapper.readValue(respStr, com.gmcc.msb.common.vo.Response.class);
                        if (!Constant.SUCCESS_RETURN_CODE.equals(returnVo.getCode())) {
                            throw new MsbException(requestReturnFailErrorCode, returnVo.toString());
                        }

                    }
                    logger.info("请求 {} {}成功", url, msg);
                } catch (IOException e) {
                    logger.error("同步{}失败，{}", msg, ExceptionUtils.getMessage(e));
                    throw new MsbException(networkFailErrorCode);
                }
            }
        } else {
            throw new MsbException(noInstantErrorCode);
        }

    }

}
