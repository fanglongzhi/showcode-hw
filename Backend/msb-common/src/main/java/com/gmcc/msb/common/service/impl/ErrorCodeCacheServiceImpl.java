package com.gmcc.msb.common.service.impl;

import com.gmcc.msb.common.entity.ErrorCode;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.service.ErrorCodeCacheService;
import com.gmcc.msb.common.service.client.MsbServiceClient;
import com.gmcc.msb.common.vo.Response;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yuan Chunhai
 * @Date 10/12/2018-6:21 PM
 */
@Service
public class ErrorCodeCacheServiceImpl implements ErrorCodeCacheService {

    private static final Logger log = LoggerFactory.getLogger(ErrorCodeCacheServiceImpl.class);

    private ConcurrentHashMap<String, ErrorCode> cache = new ConcurrentHashMap<>(100);

    @Value("${spring.application.name}")
    private String serviceId;
    @Autowired
    private MsbServiceClient msbServiceClient;
    @Autowired
    private MsbProperties msbProperties;

    @Override
    public void refreshCache() {
        log.info("刷新错误码缓存");
        Response<List<ErrorCode>> errorCodes = null;
        try {
            errorCodes = msbServiceClient.findAllInService(this.serviceId);
            cache.clear();
            for (ErrorCode errorCode : errorCodes.getContent()) {
                cache.put(errorCode.getCode(), errorCode);
            }
        } catch (Exception e) {
            log.error("获取错误码异常 {}", ExceptionUtils.getMessage(e));
            throw new MsbException(msbProperties.getServiceCode() + "-11000", e.getMessage());
        }
    }

    @Override
    public ErrorCode getErrorCode(String code) {
        ErrorCode errorCode = cache.get(code);
        if (errorCode == null) {
            errorCode = new ErrorCode(code, "未配置错误信息", "未配置错误描述");
        }
        return errorCode;
    }


    @Override
    public ErrorCode getErrorCodeWithOutServiceCode(String shortCode) {
        return getErrorCode(getErrorCodeStr(shortCode));
    }

    private String getErrorCodeStr(String code) {
        return this.msbProperties.getServiceCode() + "-" + code;
    }

    @Override
    public ErrorCode getDefault() {
        return new ErrorCode(getErrorCodeStr("00000"), "未定义错误", null);
    }


}
