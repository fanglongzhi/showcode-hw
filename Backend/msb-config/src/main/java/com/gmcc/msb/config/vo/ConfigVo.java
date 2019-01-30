package com.gmcc.msb.config.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class ConfigVo {
    private Integer id;
    @NotEmpty
    @Length(min=1,max = 1024)
    private String propertyKey;
    @NotEmpty
    @Length(min=1,max = 2048)
    private String propertyValue;
    @NotEmpty
    @Length(min=1,max = 50)
    private String application;
    @Length(max=20)
    private String label;
    @NotEmpty
    @Length(min=1,max=20)
    private String profile;

    @Length(min=1,max=3)
    private String encryptionFlag;

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

    public String getEncryptionFlag() {
        return encryptionFlag;
    }

    public void setEncryptionFlag(String encryptionFlag) {
        this.encryptionFlag = encryptionFlag;
    }
}
