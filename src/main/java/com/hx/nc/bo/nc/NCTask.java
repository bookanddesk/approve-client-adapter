package com.hx.nc.bo.nc;

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
    private String senderMan;
    private String senderName;

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
        mUrl = builder.mUrl;
        setUrl(builder.url);
        setSenderMan(builder.senderMan);
        setSenderName(builder.senderName);
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
        private String mUrl;
        private String url;
        private String senderMan;
        private String senderName;

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

        public Builder setMUrl(String mUrl) {
            this.mUrl = mUrl;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setSenderMan(String senderMan) {
            this.senderMan = senderMan;
            return this;
        }

        public Builder setSenderName(String senderName) {
            this.senderName = senderName;
            return this;
        }

        public NCTask build() {
            return new NCTask(this);
        }
    }
}
