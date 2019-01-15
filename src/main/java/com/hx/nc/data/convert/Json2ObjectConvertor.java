package com.hx.nc.data.convert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.hx.nc.data.Fields.DateField;
import com.hx.nc.data.Fields.MoneyField;
import com.hx.nc.data.annotation.Element;
import com.hx.nc.data.annotation.Ignore;
import com.hx.nc.data.name.INameRule;
import com.hx.nc.service.JsonResultService;
import com.hx.nc.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import yonyou.bpm.rest.ex.util.ClassUtils;
import yonyou.bpm.rest.ex.util.DateUtil;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author XingJiajun
 * @Date 2019/1/3 15:58
 * @Description
 */
public class Json2ObjectConvertor {
    private static Date parseDate(String datestr) {
        return DateUtil.parseDate(datestr);
    }

    private static String getBaseFieldName(Element ele, JsonNode node) {
        Class<?> clazz = ClassUtils.forName(ele.nameRule());
        if (INameRule.class.isAssignableFrom(clazz)) {
            try {
                INameRule nameRule = (INameRule) clazz.newInstance();
                return nameRule.getBaseName(node, true);
            } catch (InstantiationException | IllegalAccessException e) {
            }
        }
        return null;
    }

    private static String getElementName(Element ele, JsonNode node, String objFieldName) {
        if (StringUtils.isEmpty(ele.nameRule())) {
            return ele.name();
        } else {
            Class<?> clazz = ClassUtils.forName(ele.nameRule());
            if (INameRule.class.isAssignableFrom(clazz)) {
                try {
                    INameRule nameRule = (INameRule) clazz.newInstance();
                    return nameRule.getName(node, objFieldName);
                } catch (InstantiationException | IllegalAccessException e) {
                }
            }
            return null;
        }
    }

    private static Class<?> getRealTypeOfListElement(Field field) {
        Type genericFieldType = field.getGenericType();
        return getClass(field, genericFieldType);
    }

    private static Class<?> getClass(Field field, Type genericFieldType) {
        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            if (ArrayUtils.isNotEmpty(fieldArgTypes)) {
                return (Class<?>) fieldArgTypes[0];
            }
        }
        return field.getType();
    }

    private static Class<?> getRealTypeOfFiled(Class<?> clazz, Field field) {
        Type genericFieldType = clazz.getGenericSuperclass();
        return getClass(field, genericFieldType);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T convert(Class<T> clazz, JsonNode node) {
        if (node == null) {
            return null;
        }
        if (node instanceof TextNode && clazz == String.class) {
            return (T) node.asText();
        }
        if (node.isNull() || node.size() == 0) {
            return null;
        }

        T obj;
        String baseFieldName = "";
        try {
            obj = clazz.newInstance();
            boolean valueFromName;
            for (Class<?> cls = clazz; cls != Object.class; cls = cls.getSuperclass()) {
                Field[] fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);

                    Method setMethod = pd.getWriteMethod();
                    if (setMethod == null) {
                        continue;
                    }
                    setMethod.setAccessible(true);

                    Annotation annotation = field.getAnnotation(Ignore.class);
                    if (annotation != null) {
                        continue;
                    }

                    annotation = field.getAnnotation(Element.class);
                    String name = null;
                    boolean iscomplex = false;
                    boolean islist = false;
                    boolean fromParent = false;
                    valueFromName = false;
                    if (annotation != null) {
                        Element ele = (Element) annotation;
                        iscomplex = (ele.type() == Element.ElementType.Complex);
                        islist = (ele.type() == Element.ElementType.List);
                        fromParent = (ele.source() == Element.ElementSource.Parent);
                        valueFromName = (ele.source() == Element.ElementSource.BaseFieldName);
                        if (valueFromName) {
                            baseFieldName = getBaseFieldName(ele, node);
                        } else {
                            name = getElementName(ele, node, field.getName());
                        }
                    }

                    if (StringUtils.isEmpty(name)) {
                        name = field.getName();
                    }

                    if ((!node.has(name) || node.get(name).isNull()) && !valueFromName) {
                        continue;
                    }

                    if (iscomplex) {
                        Class<?> calais = getRealTypeOfFiled(clazz, field);
                        JsonNode jNode;
                        if (fromParent) {
                            jNode = node;
                        } else {
                            jNode = node.get(name);
                        }
                        if (jNode != null) {
                            setMethod.invoke(obj, convert(calais, jNode));
                        }
                    } else if (islist) {
                        ArrayNode arrayNode = JsonResultService.getArrayNode(node, name);
                        if (arrayNode != null) {
                            Class<?> calais = getRealTypeOfListElement(field);
                            List obits = new ArrayList(arrayNode.size());
                            for (JsonNode cNode : arrayNode) {
                                obits.add(convert(calais, cNode));
                            }
                            setMethod.invoke(obj, obits);
                        }
                    } else {
                        String value = null;
                        if (valueFromName) {
                            value = baseFieldName;
                        } else {
                            JsonNode valueNode = node.get(name);
                            if (valueNode instanceof ObjectNode) {
                                if (valueNode.has("dV")) {
                                    value = JsonResultService.toObject(valueNode.toString(), MoneyField.class).toString();
                                } else if (valueNode.has("year")) {
                                    value = JsonResultService.toObject(valueNode.toString(), DateField.class).toString();
                                }
                            } else {
                                value = valueNode.asText();
                            }
                        }
                        if (null != value && !"".equals(value)) {
                            String fieldType = field.getType().getSimpleName();
                            if ("String".equals(fieldType)) {
                                setMethod.invoke(obj, value);
                            } else if ("Date".equals(fieldType)) {
                                Date temp = parseDate(value);
                                setMethod.invoke(obj, temp);
                            } else if ("Integer".equals(fieldType)
                                    || "int".equals(fieldType)) {
                                Integer intval = Integer.parseInt(value);
                                setMethod.invoke(obj, intval);
                            } else if ("Long".equalsIgnoreCase(fieldType)) {
                                Long temp = Long.parseLong(value);
                                setMethod.invoke(obj, temp);
                            } else if ("Double".equalsIgnoreCase(fieldType)) {
                                Double temp = Double.parseDouble(value);
                                setMethod.invoke(obj, temp);
                            } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                                Boolean temp = Boolean.parseBoolean(value);
                                setMethod.invoke(obj, temp);
                            } else if ("Float".equalsIgnoreCase(fieldType)) {
                                Float temp = Float.parseFloat(value);
                                setMethod.invoke(obj, temp);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

}
