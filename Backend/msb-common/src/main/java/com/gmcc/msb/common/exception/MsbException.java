package com.gmcc.msb.common.exception;

/**
 * @author zhi fanglong
 */
public class MsbException extends RuntimeException {

    private final String code;
    private final String description;

    /**
     *
     * @param code 错误码
     */
    public MsbException(String code) {
        super(code);
        this.code = code;
        this.description = code;
    }

    /**
     *
     * @param code 错误码
     * @param description 附加描述
     */
    public MsbException(String code, String description) {
        super(code);
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }


    public String getDescription() {
        return description;
    }

}
