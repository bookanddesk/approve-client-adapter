package com.hx.nc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonResultService {
	private static Logger log= LoggerFactory.getLogger(JsonResultService.class);
	
	private static ObjectMapper mapper = new ObjectMapper();
	static{
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
		
	public static String toJson(Object obj){		
		try {
			String json = mapper.writeValueAsString(obj);
	        return json;
		} catch (JsonProcessingException e) {
			log.error("serialize object to json exception",e);
			return null;
		}
	}
	
	public static <T> T toObject(String json,Class<T> cls) throws NullPointerException{
		try {
			return mapper.readValue(json, cls);
		} catch (Throwable t) {
			log.error("deserialize json to object exception.json=["+json+"],cls="+cls.getName(),t);
			throw new RuntimeException("反序列化JSON数据失败");
		}
	}

	public static JsonNode createNode(String str){
		try {
			return mapper.readTree(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayNode createArrayNode(){
		return mapper.createArrayNode();
	}
	
	public static ObjectNode createObjectNode(){
		return mapper.createObjectNode();
	}
	
	public static String getValue(JsonNode node, String fieldName){
		return getValue(node,fieldName,null);
	}
	
	public static String getValue(JsonNode node, String fieldName, String nullValue){
		JsonNode obj = node.get(fieldName);
		if(obj==null) return nullValue;
		String txt = obj.asText();
		return "null".equals(txt)?nullValue:txt;
	}
	
	public static boolean getBoolValue(JsonNode node, String fieldName){
		JsonNode obj = node.get(fieldName);
		if(obj==null) return false;
		return obj.booleanValue();
	}
	
	public static Boolean getBooleanValue(JsonNode node, String fieldName){
		JsonNode obj = node.get(fieldName);
		if(obj==null) return null;
		return obj.booleanValue();
	}
	
	public static Integer getIntValue(JsonNode node, String fieldName){
		JsonNode obj = node.get(fieldName);
		if(obj==null || obj instanceof NullNode) return 0;
		if(obj instanceof IntNode){
			return obj.intValue();
		}
		
		String txt = obj.asText();
		try {
			return Integer.parseInt(txt);
		} catch (NumberFormatException e) {
			throw e;
		}
	}
	
	public static Date getDateValue(JsonNode node, String fieldName){
		return toDate(getValue(node,fieldName));
	}
	
	public static Date toDate(String str){
		if(StringUtils.isBlank(str)) return null;
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sf.parse(str);
		} catch (ParseException e) {
		}
		return null;
	}

	public static ArrayNode getArrayNode(JsonNode node, String arrayName) {
		JsonNode jsonNode = node.get(arrayName);
		if (jsonNode == null || jsonNode instanceof NullNode) {
			return null;
		}
		if (jsonNode instanceof ArrayNode) {
			return (ArrayNode) jsonNode;
		}

		return null;
	}

}
