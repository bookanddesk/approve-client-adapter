package com.hx.nc.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author XingJiajun
 * @Date 2018/12/28 10:56
 * @Description
 */
@Data
public class Pendings implements Serializable {
    private List<OATask> pendingList;

    public Pendings() {
    }

    private Pendings(Builder builder) {
        setPendingList(builder.pendingList);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private List<OATask> pendingList;

        private Builder() {
        }

        public Builder setPendingList(List<OATask> pendingList) {
            this.pendingList = pendingList;
            return this;
        }

        public Pendings build() {
            return new Pendings(this);
        }
    }
}
