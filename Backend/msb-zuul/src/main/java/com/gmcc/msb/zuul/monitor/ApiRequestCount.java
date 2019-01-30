package com.gmcc.msb.zuul.monitor;

import java.io.Serializable;
/**
 * @author Yuan Chunhai
 */
public class ApiRequestCount implements Serializable{

    int apiId;
    int totalCount;
    int successCount;
    int failCount;

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
}
