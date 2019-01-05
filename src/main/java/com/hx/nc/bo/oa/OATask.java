package com.hx.nc.bo.oa;

import com.hx.nc.bo.ACAEnums;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.service.NCProperties;
import com.hx.nc.utils.SpringContextUtils;
import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2018/12/27 20:24
 * @Description
 */
@Data
public class OATask extends OATaskBaseParams {
    private String title;//待办标题
    private String thirdSenderId;//第三方待办发起人主键
    private String senderName;//第三方待办发起人姓名
    private String thirdReceiverId;//第三方待办接收人主键（保证唯一）
    private String creationDate;//待办创建时间（格式：yyyy-MM-dd HH:mm:ss）
    private String content;
    private String url;
    private String h5url;
    private String appParam;

    public OATask() {
    }

    private OATask(Builder builder) {
        setRegisterCode(builder.registerCode);
        setTaskId(builder.taskId);
        setState(builder.state);
        setSubState(builder.subState);
        setTitle(builder.title);
        setThirdSenderId(builder.thirdSenderId);
        setSenderName(builder.senderName);
        setThirdReceiverId(builder.thirdReceiverId);
        setCreationDate(builder.creationDate);
        setContent(builder.content);
        setUrl(builder.url);
        setH5url(builder.h5url);
        setAppParam(builder.appParam);
    }


    public static OATask fromNCTask(NCTask ncTask) {
        return builder()
                .setRegisterCode(getOAAppCode())
                .setTaskId(ncTask.getTaskid())
                .setTitle(ncTask.getTitle())
                .setThirdSenderId(ncTask.getSenderMan())
                .setSenderName(ncTask.getSenderName())
                .setThirdReceiverId(ncTask.getCuserId())
                .setCreationDate(ncTask.getDate())
                .setState(ACAEnums.OATaskState.todo.getCode())
                .setH5url(ncTask.getMUrl())
                .build();
    }

    private static String getOAAppCode() {
       return SpringContextUtils.getBean(NCProperties.class).getRegisterCode();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String registerCode;
        private String taskId;
        private int state;
        private int subState;
        private String title;
        private String thirdSenderId;
        private String senderName;
        private String thirdReceiverId;
        private String creationDate;
        private String content;
        private String url;
        private String h5url;
        private String appParam;

        private Builder() {
        }

        public Builder setRegisterCode(String registerCode) {
            this.registerCode = registerCode;
            return this;
        }

        public Builder setTaskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder setState(int state) {
            this.state = state;
            return this;
        }

        public Builder setSubState(int subState) {
            this.subState = subState;
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
}