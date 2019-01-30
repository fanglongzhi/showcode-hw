package com.gmcc.msb.common.config;

import com.gmcc.msb.common.entity.ErrorCode;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.service.ErrorCodeCacheService;
import com.gmcc.msb.common.vo.Response;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * @author Yuan Chunhai
 */
@ControllerAdvice
@ConditionalOnProperty("msb.common.enabled")
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @Autowired
    private ErrorCodeCacheService errorCodeCacheService;


    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Response defaultErrorHandler(Exception e) {
        logger.error("错误异常", e);


        ErrorCode errorCode = errorCodeCacheService.getDefault();
        errorCode.setDescription(e.getMessage());
        return Response.notOk(errorCode);
    }

    @ExceptionHandler(value = MsbException.class)
    @ResponseBody
    public Response handleMsbException(MsbException e) {
        ErrorCode errorCode = getErrorCode(e.getCode());
        errorCode.setDescription(e.getDescription());
        return Response.notOk(errorCode);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public Response handleIllegalArgumentException(IllegalArgumentException e) {
        return Response.notOk(getErrorCode(e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Response handleMethodArgumentNotValid(MethodArgumentNotValidException notValidException) {
        List<ObjectError> allErrors = notValidException.getBindingResult().getAllErrors();
        String code = "";
        if (allErrors != null && !allErrors.isEmpty()) {
            code = allErrors.get(0).getDefaultMessage();
        }

        return Response.notOk(getErrorCode(code));
    }


    private ErrorCode getErrorCode(String code) {
        return errorCodeCacheService.getErrorCode(code);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Response handleConstraintViolationException(ConstraintViolationException ex) {
        String msg = "数据操作异常";
        Set<ConstraintViolation<?>> violatoins = ex.getConstraintViolations();
        if (violatoins != null && !violatoins.isEmpty()) {
            msg = violatoins.iterator().next().getMessage();
        }
        logger.error("数据操作异常{}", ex);
        return getResponse("20005", ex.getMessage());
    }


    @ExceptionHandler(value = TransactionSystemException.class)
    @ResponseBody
    public Response handleTransactionSystemException(TransactionSystemException ex) {
        String msg = "数据库事务异常";
        try {
            msg = ((ConstraintViolationException) ex.getOriginalException().getCause()).getConstraintViolations().iterator().next().getMessage();
        } catch (Exception e) {
            logger.error("异常{}", ex);
        }
        return Response.notOk(getErrorCode(msg));
    }

    @ExceptionHandler(value = JpaSystemException.class)
    @ResponseBody
    public Response handleJpaSystemException(JpaSystemException ex) {
        logger.error("Jpa异常", ex);
        if (ex.getRootCause() instanceof SQLException) {
            return handleSQLException((SQLException) ex.getRootCause());
        }
        return getResponse("20005", ex.getMessage());
    }

    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    @ResponseBody
    public Response handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException ex) {
        logger.error("数据访问异常", ex);
        if (ex.getRootCause() instanceof MySQLSyntaxErrorException) {
            return handleMySQLSyntaxErrorException((MySQLSyntaxErrorException) ex.getRootCause());
        }
        return getResponse("20005", ex.getMessage());
    }


    @ExceptionHandler(value = SQLException.class)
    @ResponseBody
    public Response handleSQLException(SQLException ex) {
        logger.error("SQL异常", ex);
        int sqlErrorCode = ex.getErrorCode();
        int shortErrorCode = 30000 + sqlErrorCode;
        return getResponse(String.valueOf(shortErrorCode), ex.getMessage());
    }

    @ExceptionHandler(value = MySQLSyntaxErrorException.class)
    @ResponseBody
    public Response handleMySQLSyntaxErrorException(MySQLSyntaxErrorException ex) {
        logger.error("SQL异常", ex);
        int sqlErrorCode = ex.getErrorCode();
        int shortErrorCode = 30000 + sqlErrorCode;
        return getResponse(String.valueOf(shortErrorCode), ex.getMessage());
    }


    private Response getResponse(String shourtCode, String message) {
        ErrorCode errorCode = errorCodeCacheService.getErrorCodeWithOutServiceCode(shourtCode);
        errorCode.setDescription(message);
        return Response.notOk(errorCode);
    }

}
