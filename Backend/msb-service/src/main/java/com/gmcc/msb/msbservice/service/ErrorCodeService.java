package com.gmcc.msb.msbservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.util.SignUtils;
import com.gmcc.msb.common.vo.Constant;
import com.gmcc.msb.msbservice.entity.ErrorCode;
import com.gmcc.msb.msbservice.entity.ServiceItem;
import com.gmcc.msb.msbservice.properties.AppProperties;
import com.gmcc.msb.msbservice.repository.ErrorCodeRepository;
import com.gmcc.msb.msbservice.repository.ServiceItemRepository;
import com.gmcc.msb.msbservice.vo.req.ErrorCodeReq;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuan Chunhai
 * @Date 10/12/2018-2:18 PM
 */
@Service
public class ErrorCodeService {

    private static final Logger logger = LoggerFactory.getLogger(ErrorCodeService.class);
    @Autowired
    DiscoveryClient discoveryClient;
    @Value("${spring.application.name}")
    private String serviceId;
    @Autowired
    private AppProperties properties;

    @Autowired
    @Lazy
    private MsbProperties msbProperties;

    @Autowired
    private ErrorCodeRepository errorCodeRepository;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    public ErrorCode add(ErrorCodeReq req) {

        String serviceCode = getServiceCodePrefix(req.getServiceId());
        String newErrorCode = serviceCode + req.getCode();

        ErrorCode existCode = this.isErrorCodeExists(newErrorCode);
        if (existCode != null) {
            throw new MsbException("0006-10001");
        }

        Date date = new Date();
        ErrorCode errorCode = new ErrorCode();
        BeanUtils.copyProperties(req, errorCode);
        errorCode.setCode(newErrorCode);
        errorCode.setCreateDate(date);
        errorCode.setUpdateDate(date);

        return this.errorCodeRepository.save(errorCode);
    }

    public ErrorCode update(int id, ErrorCodeReq errorCode) {
        ErrorCode exists = this.errorCodeRepository.findOne(id);
        if (exists == null) {
            throw new MsbException("0006-10002");
        }

        BeanUtils.copyProperties(errorCode, exists,
                "id", "code", "serviceId", "createDate", "updateDate");
        exists.setUpdateDate(new Date());
        return this.errorCodeRepository.save(exists);
    }

    public void delete(int id) {
        if (this.errorCodeRepository.findOne(id) == null) {
            throw new MsbException("0006-10003");
        }
        this.errorCodeRepository.delete(id);
    }

    private ErrorCode isErrorCodeExists(String errorCode) {
        return this.errorCodeRepository.findOneByCodeEquals(errorCode);
    }

    public ErrorCode find(int id) {
        ErrorCode errCode = this.errorCodeRepository.findOne(id);
        if (errCode == null) {
            throw new MsbException("0006-10004");
        }
        return errCode;
    }

    public ErrorCode findByErrorCode(String code) {
        return this.errorCodeRepository.findOneByCodeEquals(code);
    }

    public List<ErrorCode> findAllInService(String serviceId) {
        return this.errorCodeRepository.findAllByServiceIdEquals(serviceId);
    }

    public Page<ErrorCode> findAll(Pageable pageable) {
        return this.errorCodeRepository.findAll(pageable);
    }


    public String getServiceCodePrefix(String serviceId) {
        ServiceItem serviceItem = this.serviceItemRepository.findOneByServiceIdEquals(serviceId);
        if (serviceItem == null) {
            throw new MsbException("0006-10005");
        }
        if (StringUtils.isEmpty(serviceItem.getServiceCode())) {
            throw new MsbException("0006-10006");
        }
        return serviceItem.getServiceCode() + "-";
    }

    public void refreshsCache(String serviceId) {
        cacheRefresh(serviceId);
    }

    public void cacheRefresh(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (instances != null && !instances.isEmpty()) {
            logger.info("需要同步{}个节点", instances.size());
            for (ServiceInstance instance : instances) {

                OkHttpClient client = new OkHttpClient()
                                              .newBuilder()
                                              .connectTimeout(properties.getRequestRefreshErrorCodeConnectionTime(), TimeUnit.MILLISECONDS)
                                              .readTimeout(properties.getRequestRefreshErrorCodeReadTime(), TimeUnit.MILLISECONDS)
                                              .build();

                //
                String scheme = (instance.isSecure()) ? "https" : "http";
                String url = String.format("%s://%s:%s%s", scheme,
                        ((EurekaDiscoveryClient.EurekaServiceInstance) instance).getInstanceInfo().getIPAddr(),
                        instance.getPort(),
                        properties.getRefreshErrorCodePath()
                );

                String sign = SignUtils.signRequest(properties.getRefreshErrorCodePath(), "", msbProperties.getCommonSignKey());
                Request request = new Request.Builder()
                                          .url(url)
                                          .header(Constant.MSB_INNER_CALL, "true")
                                          .header(Constant.SIGN, sign)
                                          .post(RequestBody.create(
                                                  MediaType.parse("application/json; charset=utf-8"),
                                                  ""))
                                          .build();
                Response response = null;
                try {
                    logger.info("请求 {} 同步错误码", url);
                    response = client.newCall(request).execute();
                    int code = response.code();
                    if (code != 200) {
                        String respStr = response.body().string();
                        logger.error("同步错误码失败，{}, {}", code, respStr);
                        throw new MsbException("0006-10007", String.valueOf(code));
                    } else {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String respStr = response.body().string();
                        com.gmcc.msb.common.vo.Response returnVo = objectMapper.readValue(respStr, com.gmcc.msb.common.vo.Response.class);
                        if (!Constant.SUCCESS_RETURN_CODE.equals(returnVo.getCode())) {
                            throw new MsbException("0006-10007", returnVo.toString());
                        }

                    }
                    logger.info("请求 {} 同步错误码成功", url);
                } catch (IOException e) {
                    logger.error("同步错误码失败，{}", ExceptionUtils.getMessage(e));
                    throw new MsbException("0006-10008");
                }
            }
        } else {
            throw new MsbException("0006-10009");
        }
    }

}
