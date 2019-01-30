package com.gmcc.msb.msbsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Yuan Chunhai
 * @Date 12/9/2018-2:42 PM
 */
@Entity
@Table(name = "t_task_log")
public class TaskLog {

    @Id
    private int id;

    private int taskId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date runTime;

    /**
     * 0:成功，1：失败
     */
    private Integer runStatus;

    private String errorMessage;

    private Integer elasped;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getElasped() {
        return elasped;
    }

    public void setElasped(Integer elasped) {
        this.elasped = elasped;
    }
}
