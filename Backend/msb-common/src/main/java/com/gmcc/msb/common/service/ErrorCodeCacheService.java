package com.gmcc.msb.common.service;

import com.gmcc.msb.common.entity.ErrorCode;

/**
 * @author Yuan Chunhai
 * @Date 10/17/2018-6:01 PM
 */
public interface ErrorCodeCacheService {

    /**
     * 获取默认错误码
     * @return
     */
    ErrorCode getDefault();

    /**
     * 根据错误码获取错误码对象（包括错误信息）
     * @param code
     * @return
     */
    ErrorCode getErrorCode(String code);

    /**
     * 根据后5位code获取完成错误码
     * @param shortCode
     * @return
     */
    ErrorCode getErrorCodeWithOutServiceCode(String shortCode);

    /**
     * 刷新错误码
     */
    void refreshCache();
}
