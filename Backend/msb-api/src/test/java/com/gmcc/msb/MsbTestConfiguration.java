package com.gmcc.msb;

/**
 * @author Yuan Chunhai
 * @Date 10/16/2018-3:38 PM
 */

import com.gmcc.msb.common.entity.ErrorCode;
import com.gmcc.msb.common.property.RedisKeysProperties;
import com.gmcc.msb.common.vo.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MsbTestConfiguration {

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Response defaultErrorHandler(Exception e) {
        ErrorCode errorCode = new ErrorCode();
        errorCode.setCode(e.getMessage());
        errorCode.setDescription(e.getMessage());
        return Response.notOk(errorCode);
    }


    @Bean
    public RedisKeysProperties redisProperties() {
        return new RedisKeysProperties();
    }
}
