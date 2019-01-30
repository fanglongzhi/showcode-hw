package com.gmcc.msb.msbsystem.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Yuan Chunhai
 * @Date 12/11/2018-6:22 PM
 */
@Entity
@Table(name = "t_task")
public class Task {

    @Id
    private Integer id;

    private String serviceId;

    private String taskKey;

    private String taskName;

    private Date lastRunTime;

    /**
     * 0:成功，1：失败
     */
    private Integer lastRunStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public Integer getLastRunStatus() {
        return lastRunStatus;
    }

    public void setLastRunStatus(Integer lastRunStatus) {
        this.lastRunStatus = lastRunStatus;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
