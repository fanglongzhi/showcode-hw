package com.gmcc.msb.zuul.config;

import com.gmcc.msb.common.entity.ErrorCode;
import com.gmcc.msb.common.service.ErrorCodeCacheService;
import com.gmcc.msb.common.vo.Response;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Yuan Chunhai
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@Primary
public class MsbErrorController implements ErrorController {

    private static Logger logger = LoggerFactory.getLogger(MsbErrorController.class);

    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    @Autowired
    private ErrorCodeCacheService errorCodeCacheService;

    @Override
    public String getErrorPath() {
        return errorPath;
    }

    @RequestMapping(produces = "text/html")
    public void errorHtml(HttpServletRequest request,
                          HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Exception ex = getException(request);
        try {
            if (ex instanceof ZuulException) {
                status = HttpStatus.OK;
            }
            response.setHeader("content-type", "text/html; charset=UTF-8");
            response.setStatus(status.value());
            response.getWriter().write(getErrorCode(ex).toString());
        } catch (IOException e) {
            logger.error("异常", e);
        }
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<Response> error(HttpServletRequest request, HttpServletResponse response, Exception e) {

        Exception ex = getException(request);
        HttpStatus status = getStatus(request);

        if (ex instanceof ZuulException) {
            status = HttpStatus.OK;
        }

        ErrorCode errorCode = null;
        if (ex.getCause() instanceof InvalidDataAccessResourceUsageException) {

            InvalidDataAccessResourceUsageException invalidDARUExp = (InvalidDataAccessResourceUsageException) ex.getCause();

            if (invalidDARUExp.getRootCause() instanceof MySQLSyntaxErrorException) {
                MySQLSyntaxErrorException cause = (MySQLSyntaxErrorException) invalidDARUExp.getRootCause();

                errorCode = getSqlErrorCode(String.valueOf(30000 + cause.getErrorCode()));
                errorCode.setDescription(cause.getMessage());
            }

        } else if (ex.getCause() instanceof RedisConnectionFailureException) {
            RedisConnectionFailureException exp = (RedisConnectionFailureException) ex.getCause();
            errorCode = errorCodeCacheService.getErrorCodeWithOutServiceCode("20002");
            errorCode.setDescription(exp.getMessage());
        }

        if (errorCode == null) {
            if (ex.getMessage() != null && ex.getMessage().matches("[0-9]{4}\\-[0-9]{5}")) {
                errorCode = errorCodeCacheService.getErrorCode(ex.getMessage());
            } else {
                if (ex.getCause() != null) {
                    errorCode = errorCodeCacheService.getErrorCodeWithOutServiceCode("20003");
                    errorCode.setDescription(ex.getCause().getMessage());
                } else {
                    errorCode = errorCodeCacheService.getDefault();
                    errorCode.setDescription(ExceptionUtils.getMessage(ex));
                }
            }
        }

        Response body = Response.notOk(errorCode);
        return new ResponseEntity<>(body, status);
    }

    private ErrorCode getSqlErrorCode(String sqlErrorCode) {
        return errorCodeCacheService.getErrorCodeWithOutServiceCode(sqlErrorCode);
    }

    private ErrorCode getErrorCode(Exception ex) {
        ErrorCode errorCode;
        if (ex instanceof ZuulException) {
            errorCode = errorCodeCacheService.getErrorCode(ex.getMessage());
            errorCode.setDescription(((ZuulException) ex).errorCause);
        } else {
            errorCode = errorCodeCacheService.getErrorCodeWithOutServiceCode("20003");
        }
        return errorCode;
    }

    private Exception getException(HttpServletRequest request) {
        Object ex = request.getAttribute("javax.servlet.error.exception");
        if (ex == null) {
            return new Exception();
        }
        if (ex instanceof Exception) {
            logger.error("异常:{}", ExceptionUtils.getMessage((Exception) ex));
            return (Exception) ex;
        } else {
            return new Exception("服务器内部错误");
        }
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
