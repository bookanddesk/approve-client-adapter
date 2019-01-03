package com.hx.nc.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XingJiajun
 * @Date 2018/12/27 20:35
 * @Description
 */
@Data
public class NCTask implements Serializable {
    private String billId ;
    private String billNo ;
    private String billType ;
    private String cuserId ;
    private String date ;
    private String taskid ;
    private String title ;
    private String mUrl;
    private String url;

    public NCTask() {
    }

    private NCTask(Builder builder) {
        setBillId(builder.billId);
        setBillNo(builder.billNo);
        setBillType(builder.billType);
        setCuserId(builder.cuserId);
        setDate(builder.date);
        setTaskid(builder.taskid);
        setTitle(builder.title);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String billId;
        private String billNo;
        private String billType;
        private String cuserId;
        private String date;
        private String taskid;
        private String title;

        private Builder() {
        }

        public Builder setBillId(String billId) {
            this.billId = billId;
            return this;
        }

        public Builder setBillNo(String billNo) {
            this.billNo = billNo;
            return this;
        }

        public Builder setBillType(String billType) {
            this.billType = billType;
            return this;
        }

        public Builder setCuserId(String cuserId) {
            this.cuserId = cuserId;
            return this;
        }

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setTaskid(String taskid) {
            this.taskid = taskid;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public NCTask build() {
            return new NCTask(this);
        }
    }
}
