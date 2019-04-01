package com.hx.nc.bo.oa;

import com.hx.nc.bo.ACAEnums;
import com.hx.nc.bo.nc.NCTask;
import com.hx.nc.service.NCProperties;
import com.hx.nc.utils.SpringContextUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private String content;//原生app的下载地址
    private String url;//PC穿透地址
    private String h5url;//H5穿透地址
    private String appParam;
    private String classify;//类别
    private String contentType;//内容类型
    private String noneBindingSender;//登录名称/人员编码/手机号/电子邮件
    private String noneBindingReceiver;

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
        setClassify(builder.classify);
        setContentType(builder.contentType);
        setNoneBindingSender(builder.noneBindingSender);
        setNoneBindingReceiver(builder.noneBindingReceiver);
    }


    public static OATask fromNCTask(NCTask ncTask) {
        return builder()
                .setRegisterCode(getOAAppCode())
                .setTaskId(ncTask.getTaskid())
                .setTitle(ncTask.getTitle())
                .setThirdSenderId(ncTask.getSenderMan())
                .setSenderName(ncTask.getSenderName())
                .setThirdReceiverId(ncTask.getCheckManCode())
                .setNoneBindingReceiver(ncTask.getCheckManCode())
//                .setNoneBindingSender(ncTask.getSenderName())
                .setCreationDate(ncTask.getDate())
                .setState(ACAEnums.OATaskState.todo.getCode())
                .setH5url(ncTask.getMUrl())
                .build();
    }

    public static List<OATask> fromNCTask(List<NCTask> ncTasks, Map<String, OAUser> oaUserMap) {
        List<OATask> oaTasks = new ArrayList<>(ncTasks.size());
        for (NCTask task : ncTasks) {
            Builder builder = builder()
                    .setRegisterCode(getOAAppCode())
                    .setTaskId(task.getTaskid())
                    .setTitle(task.getTitle())
                    .setState(ACAEnums.OATaskState.todo.getCode())
                    .setH5url(task.getMUrl())
                    .setCreationDate(task.getDate());
            String ncUserId = task.getCuserId();
            OAUser oaUser = oaUserMap.get(ncUserId);
            if (oaUser != null) {
                builder.setThirdReceiverId(oaUser.getCode())
                        .setNoneBindingReceiver(oaUser.getCode());
            } else {
                builder.setThirdReceiverId(task.getCheckManCode())
                        .setNoneBindingReceiver(task.getCheckManCode());
            }
            String ncSenderManId = task.getSenderMan();
            oaUser = oaUserMap.get(ncSenderManId);
            if (oaUser != null) {
                builder.setThirdSenderId(oaUser.getCode())
                        .setSenderName(oaUser.getName())
                        .setNoneBindingSender(oaUser.getCode());
            } else {
                builder.setThirdSenderId(ncSenderManId)
//                        .setNoneBindingSender(ncTask.getSenderName())
                        .setSenderName(task.getSenderName());
            }
            oaTasks.add(builder.build());

        }
        return oaTasks;
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
        private String classify;
        private String contentType;
        private String noneBindingSender;
        private String noneBindingReceiver;

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

        public Builder setClassify(String classify) {
            this.classify = classify;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setNoneBindingSender(String noneBindingSender) {
            this.noneBindingSender = noneBindingSender;
            return this;
        }

        public Builder setNoneBindingReceiver(String noneBindingReceiver) {
            this.noneBindingReceiver = noneBindingReceiver;
            return this;
        }

        public OATask build() {
            return new OATask(this);
        }
    }
}
