package com.gmcc.msb.msbservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbservice.entity.ServiceLog;
import com.gmcc.msb.msbservice.properties.AppProperties;
import com.gmcc.msb.msbservice.repository.ServiceLogRepository;
import com.gmcc.msb.msbservice.vo.ServiceLogResp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuan Chunhai
 */
@Service
public class ServiceLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogService.class);

    @Autowired
    ServiceLogRepository serviceLogRepository;

    @Autowired
    AppProperties properties;

    ObjectMapper objectMapper = new ObjectMapper();


    public Page<ServiceLog> find(Pageable pageable) {
        List<Long> orgIds = UserContextHolder.getContext().getDataOrgList();
        if(orgIds.size()==0){

            return new PageImpl<ServiceLog>(new ArrayList<>(),pageable,0);
        }

        return serviceLogRepository.findServiceLogByOrgs(orgIds,pageable);
    }

    public ServiceLog add(ServiceLog serviceLog) {
        ServiceLog one = serviceLogRepository.findOneByTimeEqualsAndContentEquals(serviceLog.getTime(), serviceLog.getContent());
        if (one == null) {
            return serviceLogRepository.save(serviceLog);
        }
        return null;
    }


    public ServiceLogResp getEurekaRegisterLog() {

        OkHttpClient client = new OkHttpClient()
                                      .newBuilder()
                                      .connectTimeout(properties.getRequestEurekaConnectionTime(), TimeUnit.MILLISECONDS)
                                      .readTimeout(properties.getRequestEurekaReadTime(), TimeUnit.MILLISECONDS)
                                      .build();

        String url = this.properties.getEurekaRegisterLogUrl();

        Request request = new Request.Builder()
                                  .url(url)
                                  .get()
                                  .header("content-type", "application/json")
                                  .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();
            int code = response.code();
            if (code != HttpStatus.OK.value()) {
                String respStr = response.body().string();
                throw new MsbException("0006-10010", code + "," + respStr);
            } else {
                String resp = response.body().string();
                return parseJson(resp);
            }
        } catch (IOException e) {
            LOGGER.error("同步路由配置失败，{}", ExceptionUtils.getMessage(e));
            throw new MsbException("0006-10011");
        }
    }

    private ServiceLogResp parseJson(String resp) {
        try {
            return objectMapper.readValue(resp, ServiceLogResp.class);
        } catch (Exception e) {
            LOGGER.error("请求服务日志转换返回内容失败,{},{}", resp, e);
            throw new MsbException("0006-10012", e.getMessage());
        }
    }


}
