package com.gmcc.msb.msbservice.vo;

import java.util.List;
/**
 * @author zhi fanglong
 */
public class ServiceLogResp {

    private List<Data> lastNCanceled;
    private List<Data> lastNRegistered;

    public List<Data> getLastNCanceled() {
        return lastNCanceled;
    }

    public void setLastNCanceled(List<Data> lastNCanceled) {
        this.lastNCanceled = lastNCanceled;
    }

    public List<Data> getLastNRegistered() {
        return lastNRegistered;
    }

    public void setLastNRegistered(List<Data> lastNRegistered) {
        this.lastNRegistered = lastNRegistered;
    }

    public static class Data {
        private long date;
        private String id;

        public Data(){}

        public Data(long date, String id){
            this.date = date;
            this.id = id;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Data{" +
                           "date=" + date +
                           ", id='" + id + '\'' +
                           '}';
        }
    }

}
