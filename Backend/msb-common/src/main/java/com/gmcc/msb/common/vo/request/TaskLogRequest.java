package com.gmcc.msb.common.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author Yuan Chunhai
 * @Date 12/12/2018-10:52 AM
 */
public class TaskLogRequest {

    public static final int  SUCCESS = 0;
    public static final int  FAIL = 1;

    private String serviceId;

    private String taskKey;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date runTime;

    /** 0:成功，1：失败 */
    private int runStatus ;

    /** 耗时 */
    private int elasped;

    private String errorMessage;

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public int getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(int runStatus) {
        this.runStatus = runStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getElasped() {
        return elasped;
    }

    public void setElasped(int elasped) {
        this.elasped = elasped;
    }
}
