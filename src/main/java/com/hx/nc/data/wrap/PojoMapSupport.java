package com.hx.nc.data.wrap;

import com.hx.nc.data.annotation.Element;
import com.hx.nc.data.annotation.Ignore;
import com.hx.nc.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:23
 * @Description
 */
public class PojoMapSupport implements I2MapSupport {
    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = getClass();
        for (Class<?> cls = clazz; cls != Object.class; cls = cls
                .getSuperclass()) {

            Field fields[] = cls.getDeclaredFields();
            if (fields == null || fields.length == 0)
                continue;
            String name;
            Object value;
            try {
                Field.setAccessible(fields, true);
                Annotation annotation;
                for (int i = 0; i < fields.length; i++) {
                    annotation = fields[i].getAnnotation(Ignore.class);
                    if (annotation != null)
                        continue;
                    annotation = fields[i].getAnnotation(Element.class);
                    name = null;
                    if (annotation != null) {
                        Element ele = (Element) annotation;
                        name = ele.name();
                    }
                    if (StringUtils.isEmpty(name)) {
                        name = fields[i].getName();
                    }
                    value = fields[i].get(this);
                    map.put(name, value);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

}
