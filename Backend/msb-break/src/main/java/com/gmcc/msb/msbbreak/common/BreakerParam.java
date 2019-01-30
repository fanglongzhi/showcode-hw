package com.gmcc.msb.msbbreak.common;

public class BreakerParam {
    private BreakerParam(){}
    public static final String PARAM_PRE = "hystrix.command";
    public static final String DEFAULT_KEY="default";

    public static final String ENABLE_BREAKER_SUF = "circuitBreaker.enabled";
    public static final String REQUEST_VOLUME_SUF = "circuitBreaker.requestVolumeThreshold";
    public static final String SLEEP_SUF="circuitBreaker.sleepWindowInMilliseconds";
    public static final String FAIL_RATE_SUF="circuitBreaker.errorThresholdPercentage";
    public static final String TIMEOUT_SUF="execution.isolation.thread.timeoutInMilliseconds";

    public static final String ENABLE_BREAKER_TYPE = "circuitBreakerEnabled";
    public static final String REQUEST_VOLUME_TYPE = "requestVolumeThreshold";
    public static final String SLEEP_TYPE="sleepWindowInMilliseconds";
    public static final String FAIL_RATE_TYPE="errorThresholdPercentage";
    public static final String TIMEOUT_TYPE="timeoutInMilliseconds";

}
