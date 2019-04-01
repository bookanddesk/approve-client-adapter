package com.hx.nc.bo.nc;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author XingJiajun
 * @Date 2019/4/1 13:56
 * @Description
 */
@Data
public class NCFileDataParams extends NCBaseParams {
    @NotBlank
    private String fileId;
    @NotBlank
    private String filename;
}
