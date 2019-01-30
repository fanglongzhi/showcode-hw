package com.gmcc.msb.msbsystem.entity.org;

import java.util.Map;

public class EOMSRequestEntity {
    //传操作名：SMS_SEND_MESSAGE_REMEDY
    String operation;
    //传系统名，如：OSSO，自定义
    String system = "MSB";
    Map<String,String> data;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
