package com.hx.nc.bo;

import com.hx.nc.service.JsonResultService;

import java.io.Serializable;

public class JsonResult implements Serializable {

    private String flag;

    private String desc;

    private Object data;

    private String costTime;

    private JsonResult(String flag, String desc, Object data) {
        super();
        this.flag = flag;
        this.desc = desc;
        this.data = data == null ? new Object() : data;
    }


    public static JsonResult successResult() {
        return new JsonResult("0", "OK", null);
    }

    public static JsonResult successResult(Object data) {
        return new JsonResult("0", "OK", data);
    }

    public static JsonResult failResult(Object data) {
        return new JsonResult("1", "Error", data);
    }

    public static JsonResult failResult(String msg, Object data) {
        return new JsonResult("1", msg, data);
    }

    public static JsonResult failResult(String errorMsg){
        return new JsonResult("1", errorMsg, null);
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data == null ? new Object() : data;
    }

    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    @Override
    public String toString() {
        return JsonResultService.toJson(this);
    }


}
