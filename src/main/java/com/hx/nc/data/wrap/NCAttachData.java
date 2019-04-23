package com.hx.nc.data.wrap;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:43
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class NCAttachData extends PojoMapSupport {
    @EqualsAndHashCode.Include
    private String fileid;
    private String filename;
    private String filesize;
    private String downflag;
}
