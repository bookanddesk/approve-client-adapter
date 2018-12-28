package com.hx.nc.bo;

import com.hx.nc.service.NCProperties;
import com.hx.nc.utils.SpringContextUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @author XingJiajun
 * @Date 2018/12/27 20:24
 * @Description
 */
@Data
public class OATask implements Serializable {
    private String taskId;
    private String registerCode;
    private String title;
    private String thirdSenderId;
    private String senderName;
    private String thirdReceiverId;
    private String creationDate;
    private String state;
    private String subState;
    private String content;
    private String url;
    private String h5url;
    private String appParam;

    public OATask() {
    }

    private OATask(Builder builder) {
        setTaskId(builder.taskId);
        setRegisterCode(builder.registerCode);
        setTitle(builder.title);
        setThirdSenderId(builder.thirdSenderId);
        setSenderName(builder.senderName);
        setThirdReceiverId(builder.thirdReceiverId);
        setCreationDate(builder.creationDate);
        setState(builder.state);
        setSubState(builder.subState);
        setContent(builder.content);
        setUrl(builder.url);
        setH5url(builder.h5url);
        setAppParam(builder.appParam);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String taskId;
        private String registerCode;
        private String title;
        private String thirdSenderId;
        private String senderName;
        private String thirdReceiverId;
        private String creationDate;
        private String state;
        private String subState;
        private String content;
        private String url;
        private String h5url;
        private String appParam;

        private Builder() {
        }

        public Builder setTaskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder setRegisterCode(String registerCode) {
            this.registerCode = registerCode;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setThirdSenderId(String thirdSenderId) {
            this.thirdSenderId = thirdSenderId;
            return this;
        }

        public Builder setSenderName(String senderName) {
            this.senderName = senderName;
            return this;
        }

        public Builder setThirdReceiverId(String thirdReceiverId) {
            this.thirdReceiverId = thirdReceiverId;
            return this;
        }

        public Builder setCreationDate(String creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setSubState(String subState) {
            this.subState = subState;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setH5url(String h5url) {
            this.h5url = h5url;
            return this;
        }

        public Builder setAppParam(String appParam) {
            this.appParam = appParam;
            return this;
        }

        public OATask build() {
            return new OATask(this);
        }
    }

    public static OATask fromNCTask(NCTask ncTask) {
        return newBuilder()
                .setRegisterCode(getOAAppCode())
                .setTaskId(ncTask.getTaskid())
                .setTitle(ncTask.getTitle())
                .setSenderName(null)
                .setThirdReceiverId(ncTask.getCuserId())
                .setCreationDate(ncTask.getDate())
                .setState(Constant.zero_string_value)
                .build();
    }

    private static String getOAAppCode() {
       return SpringContextUtils.getBean(NCProperties.class).getRegisterCode();
    }
}
