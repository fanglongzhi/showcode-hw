package com.gmcc.msb.msbservice.entity;

import javax.persistence.*;
/**
 * @author zhi fanglong
 */
@Entity
@Table(name = "t_service_log")
public class ServiceLog {

    public static final int TYPE_REGISTER = 0;
    public static final int TYPE_CACEL = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private Long time;
    /** 0：注册，1：取消 */
    private Integer type;

    @Transient
    private String typeName;

    private String serviceId;

    private String content;

    public ServiceLog() {
        // default
    }

    public ServiceLog(Long time, Integer type, String serviceId, String content) {
        this.time = time;
        this.type = type;
        this.serviceId = serviceId;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypeName() {
        if (TYPE_REGISTER == this.type){
            return "注册";
        }
        else if (TYPE_CACEL == this.type){
            return "取消";
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
