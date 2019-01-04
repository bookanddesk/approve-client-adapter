package com.hx.nc.data.wrap;

import lombok.Data;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:43
 * @Description
 */
@Data
public class NCAttachData extends PojoMapSupport {
    private String fileid;
    private String filename;
    private String filesize;
    private String downflag;
}
