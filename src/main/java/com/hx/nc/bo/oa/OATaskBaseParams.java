package com.hx.nc.bo.oa;

import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/5 10:13
 * @Description
 */
@Data
public class OATaskBaseParams extends OABaseParam {
    private String taskId; //第三方待办主键（保证唯一）
    private int state; //状态：0:未办理；1:已办理
    private int subState; //处理后状态：0/1/2/3同意已办/不同意已办/取消/驳回

    public OATaskBaseParams() {
    }

    private OATaskBaseParams(Builder builder) {
        setRegisterCode(builder.registerCode);
        setTaskId(builder.taskId);
        setState(builder.state);
        setSubState(builder.subState);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String registerCode;
        private String taskId;
        private int state;
        private int subState;

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

        public OATaskBaseParams build() {
            return new OATaskBaseParams(this);
        }
    }
}
