package com.gmcc.msb.config.entity;

import com.gmcc.msb.config.vo.ConfigVo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="t_config")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String propertyKey;
    private String propertyValue;
    private String application;
    private String label = "master";
    private String profile;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;

    public Config(){}
    public Config(ConfigVo configVo){
        this.application=configVo.getApplication();
        this.id = configVo.getId();
        this.label=configVo.getLabel()==null?"master":configVo.getLabel();
        this.profile=configVo.getProfile();
        this.propertyKey=configVo.getPropertyKey();
        this.propertyValue=configVo.getPropertyValue();

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
