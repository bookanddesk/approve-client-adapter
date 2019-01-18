package com.hx.nc.bo;

/**
 * @author XingJiajun
 * @Date 2019/1/5 10:37
 * @Description
 */
public class ACAEnums {

    //处理后状态：0/1/2/3同意已办/不同意已办/取消/驳回
    public enum OATaskSubState {
        agreeDone(0, "同意已办"),
        disagreeDone(1, "不同意已办"),
        cancel(2, "取消"),
        reject(3, "驳回");

        private int code;
        private String value;

        OATaskSubState(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return this.code;
        }
    }

    //状态：0:未办理；1:已办理
    public enum OATaskState {
        todo(0, "未办理"),
        down(1, "已办理");

        private int code;
        private String value;

        OATaskState(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return this.code;
        }

    }

    public enum action {
        agree("同意") {
            @Override
            public int taskNextState() {
                return OATaskState.down.code;
            }

            @Override
            public int taskNextSubState() {
                return OATaskSubState.agreeDone.code;
            }
        },
        reject("驳回") {
            @Override
            public int taskNextState() {
                return OATaskState.down.code;
            }

            @Override
            public int taskNextSubState() {
                return OATaskSubState.reject.code;
            }
        },
        disagree("不同意") {
            @Override
            public int taskNextState() {
                return OATaskState.down.code;
            }

            @Override
            public int taskNextSubState() {
                return OATaskSubState.disagreeDone.code;
            }
        };

        private String value;

        action(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public abstract int taskNextState();

        public abstract int taskNextSubState();

    }


}
