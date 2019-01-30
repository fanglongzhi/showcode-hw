package com.gmcc.msb.config.vo;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class RefreshResultVo {
  private int amount;
  private int failAmount;
  private List<Map<String,String>> successList;
  private List<Map<String,String>> failList;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Map<String,String>> getSuccessList() {
        if(successList==null){
            this.successList= Lists.newArrayList();
        }
        return successList;
    }


    public List<Map<String,String>> getFailList() {
        if(failList==null){
            this.failList=Lists.newArrayList();
        }
        return failList;
    }

    public int getFailAmount() {
        return getFailList().size();
    }

}
