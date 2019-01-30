package com.gmcc.msb.api.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhi Fanglong
 */
public class CommonConstant {

    /**
     * 应用类型
     */
    public static final String APP = "app";

    /**
     * 请求参数有误
     */
    public static final String ERROR_REQUEST_DATA_CODE = "0001-00001";
    /**
     * ID不能为空
     */
    public static final String ERROR_ID_MISS_CODE = "0001-00002";
    /**
     * 审核结果不能为空
     */
    public static final String ERROR_AUDIT_RESULT_MISS_CODE = "0001-00003";
    /**
     * 应用已经注册
     */
    public static final String ERROR_APP_EXIST_CODE = "0001-10001";
    /**
     * 记录不存在
     */
    public static final String ERROR_RECORD_NOT_EXIST_CODE = "0001-10002";
    /**
     * 该应用已订阅了API
     */
    public static final String ERROR_APP_HAS_SUBSCRIBE_CODE = "0001-10003";
    /**
     * 该APP正处于审核中
     */
    public static final String ERROR_APP_AUDITING_CODE = "0001-10004";
    /**
     * 该状态不能申请生效
     */
    public static final String ERROR_WRONG_STATUS_CODE = "0001-10005";
    /**
     * 审核的数据不存在
     */
    public static final String ERROR_AUDIT_DATA_NOT_EXIST_CODE = "0001-10006";
    /**
     * 应用数据不存在
     */
    public static final String ERROR_APP_NOT_EXIST_CODE = "0001-10007";
    /**
     * app的状态不是审核中
     */
    public static final String ERROR_NOT_AUDITING_CODE = "0001-10008";

    /**
     * 应用已经设置了流控策略
     */
    public static final String ERROR_FLUID_STRATEGY_EXIST = "0001-10020";
    public static final Map<String, String> CODE_MSG_MAP = new HashMap<>();

    public static final String ERROR_VALIDATE_LOG_MSG = "数据约束验证失败:{}";

    static {
        CODE_MSG_MAP.put(ERROR_REQUEST_DATA_CODE, "请求参数有误");
        CODE_MSG_MAP.put(ERROR_ID_MISS_CODE, "ID不能为空");
        CODE_MSG_MAP.put(ERROR_AUDIT_RESULT_MISS_CODE, "审核结果不能为空");
        CODE_MSG_MAP.put(ERROR_APP_EXIST_CODE, "应用已经注册");
        CODE_MSG_MAP.put(ERROR_RECORD_NOT_EXIST_CODE, "记录不存在");
        CODE_MSG_MAP.put(ERROR_APP_HAS_SUBSCRIBE_CODE, "该应用已订阅了API");
        CODE_MSG_MAP.put(ERROR_APP_AUDITING_CODE, "该APP正处于审核中");
        CODE_MSG_MAP.put(ERROR_WRONG_STATUS_CODE, "该状态不能申请生效");
        CODE_MSG_MAP.put(ERROR_AUDIT_DATA_NOT_EXIST_CODE, "核的数据不存在");
        CODE_MSG_MAP.put(ERROR_APP_NOT_EXIST_CODE, "应用数据不存在");
        CODE_MSG_MAP.put(ERROR_NOT_AUDITING_CODE, "app的状态不是审核中");

    }

    private CommonConstant() {
    }


}
