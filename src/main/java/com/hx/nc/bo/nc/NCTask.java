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
    private String checkManCode ;//nc代办人编码，值和oa人员编码相同
    private String cuserId ;//nc待办人id
    private String date ;
    private String taskid ;
    private String title ;
    private String mUrl;
    private String url;
    private String senderMan;//nc任务发送人id
    private String senderName;//任务发送人姓名
}
