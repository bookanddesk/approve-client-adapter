package com.hx.nc.data.name;

import com.fasterxml.jackson.databind.JsonNode;
import com.hx.nc.bo.Constants;

import java.util.Iterator;

/**
 * @author XingJiajun
 * @Date 2019/1/3 10:59
 * @Description
 */
public class BillItemNameRule implements INameRule {
    @Override
    public String getName(JsonNode node, String name) {
        String result = null;
        switch (name) {
            case Constants.NC_DATA_FIELD_SHOW_NAME:
                result = getBaseName(node, false);
                break;
            case Constants.NC_DATA_FIELD_SHOW_VALUE:
                result = getBaseName(node, true);
                break;
            case Constants.NC_DATA_FIELD_SHOW_VALUE_ID:
                result = getBaseName(node, true) + Constants.NC_DATA_FIELD_SUFFIX_ID;
        }
        return result;
    }

    @Override
    public String getBaseName(JsonNode node, boolean sub) {
        Iterator<String> fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            if (fieldName.endsWith(Constants.NC_DATA_FIELD_SUFFIX_ITEM_SHOW_NAME)) {
                return
                        sub ? fieldName.substring(0, fieldName.length() - Constants.NC_DATA_FIELD_SUFFIX_ITEM_SHOW_NAME_LENGTH) :
                                fieldName;
            }
        }
        return null;
    }


}
