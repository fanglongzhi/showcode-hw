package com.gmcc.msb.config;

import com.gmcc.msb.common.entity.ErrorCode;
import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.vo.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Yuan Chunhai
 * @Date 11/7/2018-11:40 AM
 */
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
    public MsbProperties msbProperties(){
        return new MsbProperties();
    }

}
