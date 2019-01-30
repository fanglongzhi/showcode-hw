package com.gmcc.msb.msbbreak.common;

public class CommonConstant {
    private CommonConstant(){}
    public static final String ERROR_CODE = "1";
    public static final String ERROR_MSG = "数据不存在";
    public static final String ERROR_VALIDATE_CODE = "0009-00001";
    public static final String ERROR_VALIDATE_LOG_MSG = "数据约束验证失败:{}";
    public static final String ERROR_VALIDATE_MSG = "策略名称和是否默认必须输入";
    public static final String ERROR_NO_EXIST_MSG="api或熔断策略不存在";
    public static final String ERROR_DEFAULT_MSG="不能绑定默认策略";

    public static final String ERROR_STRATEGY_NOT_EXIST="0009-10001";
    public static final String ERROR_MISSING_STRATIGY_ID="0009-00002";
    public static final String ERROR_API_NOT_EXIST="0009-10002";
    public static final String ERROR_IS_DEFAULT="0009-10003";
    public static final String ERROR_DEFAULT_CAN_NOT_BIND="0009-10004";
    public static final String ERROR_STRATEGY_NAME_DUPLICATE="0009-10005";


}
