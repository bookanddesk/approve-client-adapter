package com.hx.nc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.hx.nc.bo.Constant;
import com.hx.nc.service.JsonResultService;

import java.io.*;
import java.util.HashMap;

/**
 * @author XingJiajun
 * @Date 2018/12/28 16:38
 * @Description
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    public static String getLastPollDateFromJsonFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getLPDFileName()))) {
            JsonNode jsonNode = JsonResultService.createNode(bufferedReader.readLine());
            if (jsonNode != null) {
                return JsonResultService.getValue(jsonNode, Constant.LAST_POLL_DATE_TIME);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void recordLastPollDateToJsonFile(String lastPollDate) {
        if (StringUtils.isEmpty(lastPollDate)) {
            return;
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(getLPDFileName()))) {
            bufferedWriter.write(JsonResultService.toJson(new HashMap() {{
                put(Constant.LAST_POLL_DATE_TIME, lastPollDate);
            }}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLPDFileName() {
        return new StringBuilder(new File(FileUtils.class.getResource("/").getPath()).getParent())
                .append(File.separator)
                .append("classes")
                .append(File.separator)
                .append("lpd.json")
                .toString();
    }

//    public static String getLastPollDateFromFile() {
//        String lastPollDate = null;
//        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getLPDFileName()))) {
//            String temp = null;
//            do {
//                lastPollDate = temp;
//            } while ((temp = bufferedReader.readLine()) != null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return lastPollDate;
//    }


//    public static void recordLastPollDateToFile(String lastPollDate) {
//        if (StringUtils.isEmpty(lastPollDate)) {
//            return;
//        }
//        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(getLPDFileName(), true))) {
//            bufferedWriter.write(new StringBuilder("\n").append(lastPollDate).toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
