package com.hx.nc.bo.oa;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author XingJiajun
 * @Date 2019/3/30 10:57
 * @Description
 */
@Data
//@Entity
@NoArgsConstructor(force = true)
public class OAUser {
    @Id
    @Column(name = "nc_id")
    private String ncId;
    private String code;
    private String name;

    @Builder
    public OAUser(String ncId, String code, String name) {
        this.ncId = ncId;
        this.code = code;
        this.name = name;
    }
}
