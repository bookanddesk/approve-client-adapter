package com.hx.nc.data.Fields;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/14 20:28
 * @Description
 */
@Data
public class MoneyField {
    @JsonProperty("dV")
    private List<Integer> dV;
    @JsonProperty("double")
    private double _double;
    private int power;
    private int sIValue;
    private boolean trimZero;

    @Override
    public String toString() {
        return String.valueOf(_double);
    }
}
