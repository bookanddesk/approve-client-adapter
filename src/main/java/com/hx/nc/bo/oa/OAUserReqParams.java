package com.hx.nc.bo.oa;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/3/30 9:31
 * @Description
 */
@Data
@NoArgsConstructor(force = true)
public class OAUserReqParams {
    private List<String> userzj;

    @Builder
    public OAUserReqParams(List<String> userzj) {
        this.userzj = userzj;
    }
}
