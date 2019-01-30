package com.gmcc.msb.api.vo.response;

/**
 * @author Yuan Chunhai
 * @Date 11/28/2018-5:48 PM
 */
public class UsageData {

    private int apiCount;

    private int appCount;

    private int serviceCount;


    public int getApiCount() {
        return apiCount;
    }

    public void setApiCount(int apiCount) {
        this.apiCount = apiCount;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }

    public int getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(int serviceCount) {
        this.serviceCount = serviceCount;
    }
}
