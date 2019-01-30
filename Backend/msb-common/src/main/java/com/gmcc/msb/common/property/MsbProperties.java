package com.gmcc.msb.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.gmcc.msb.common.property.MsbProperties.PREFIX;

/**
 * @author Yuan Chunhai
 * @Date 9/29/2018-11:05 AM
 */
@Component
@ConfigurationProperties(prefix = PREFIX)
@RefreshScope
public class MsbProperties {

    public static final String PREFIX = "msb";

    private String signKey = "msb-common";
    private String commonSignKey = "5OTAwMDAwLAogICJ1c2VyX2";
    private boolean enabledSignFilter = true;
    private List<String> skipSignCheckUri = Arrays.asList("/error");
    private List<String> signCheckController = new ArrayList<>();

    private String serviceCode = "0000";
    private Map<String, String> redisKey = new HashMap<>();

    private int executorServicePoolSize = 1;
    private int taskSchedulerPoolSize = 5;

    private boolean enabledMonitor = false;
    private boolean enabledTests = false;

    private String testQuerySql = "select * from t_service_api";
    private List<String> testQuerySqlResultFileds = Arrays.asList("id", "api_name");

    private String testInsertSql = "INSERT INTO `t_role` (`role_name`) VALUES ('test_role')\n";


    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public boolean isEnabledSignFilter() {
        return enabledSignFilter;
    }

    public void setEnabledSignFilter(boolean enabledSignFilter) {
        this.enabledSignFilter = enabledSignFilter;
    }

    public List<String> getSkipSignCheckUri() {
        return skipSignCheckUri;
    }

    public void setSkipSignCheckUri(List<String> skipSignCheckUri) {
        this.skipSignCheckUri = skipSignCheckUri;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Map<String, String> getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(Map<String, String> redisKey) {
        this.redisKey = redisKey;
    }

    public String getCommonSignKey() {
        return commonSignKey;
    }

    public void setCommonSignKey(String commonSignKey) {
        this.commonSignKey = commonSignKey;
    }

    public List<String> getSignCheckController() {
        return signCheckController;
    }

    public void setSignCheckController(List<String> signCheckController) {
        this.signCheckController = signCheckController;
    }

    public int getExecutorServicePoolSize() {
        return executorServicePoolSize;
    }

    public void setExecutorServicePoolSize(int executorServicePoolSize) {
        this.executorServicePoolSize = executorServicePoolSize;
    }

    public int getTaskSchedulerPoolSize() {
        return taskSchedulerPoolSize;
    }

    public void setTaskSchedulerPoolSize(int taskSchedulerPoolSize) {
        this.taskSchedulerPoolSize = taskSchedulerPoolSize;
    }

    public boolean isEnabledMonitor() {
        return enabledMonitor;
    }

    public void setEnabledMonitor(boolean enabledMonitor) {
        this.enabledMonitor = enabledMonitor;
    }

    public String getTestQuerySql() {
        return testQuerySql;
    }

    public void setTestQuerySql(String testQuerySql) {
        this.testQuerySql = testQuerySql;
    }

    public List<String> getTestQuerySqlResultFileds() {
        return testQuerySqlResultFileds;
    }

    public void setTestQuerySqlResultFileds(List<String> testQuerySqlResultFileds) {
        this.testQuerySqlResultFileds = testQuerySqlResultFileds;
    }

    public String getTestInsertSql() {
        return testInsertSql;
    }

    public void setTestInsertSql(String testInsertSql) {
        this.testInsertSql = testInsertSql;
    }

    public boolean isEnabledTests() {
        return enabledTests;
    }

    public void setEnabledTests(boolean enabledTests) {
        this.enabledTests = enabledTests;
    }
}
