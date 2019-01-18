package com.hx.nc.data;

import com.hx.nc.data.entity.OARestRecord;

import javax.persistence.AttributeConverter;

/**
 * @author XingJiajun
 * @Date 2019/1/17 14:40
 * @Description
 */
public class OARestTypeConverter implements AttributeConverter<OARestRecord.Type, String> {
    @Override
    public String convertToDatabaseColumn(OARestRecord.Type type) {
        return type.name();
    }

    @Override
    public OARestRecord.Type convertToEntityAttribute(String s) {
        return OARestRecord.Type.valueOf(s);
    }
}
