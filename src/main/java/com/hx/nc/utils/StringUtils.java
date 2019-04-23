package com.hx.nc.utils;

import com.google.common.base.Joiner;
import com.hx.nc.bo.Constants;

import java.util.Optional;

/**
 * @author XingJiajun
 * @Date 2018/11/9 19:17
 * @Description
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static boolean equalsWithNullBlank(String s1, String s2) {
        return Optional.ofNullable(s1).orElse("").equals(Optional.ofNullable(s2).orElse(""));
    }

    public static <E> String join_ (E... els) {
        return Joiner.on(Constants.UNDERLINE).skipNulls().join(els);
    }


}
