package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hx.nc.bo.nc.NCBillDetailParams;
import com.hx.nc.data.bill.*;
import com.hx.nc.data.bpm.*;
import com.hx.nc.data.convert.DateSwap;
import com.hx.nc.data.convert.FileDataConvector;
import com.hx.nc.data.wrap.*;
import com.hx.nc.data.wrap.response.*;
import com.hx.nc.utils.DateTimeUtils;
import com.hx.nc.utils.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yonyou.bpm.rest.ex.util.DateUtil;
import yonyou.bpm.rest.response.CommentResponse;
import yonyou.bpm.rest.response.form.FormFieldResponse;
import yonyou.bpm.rest.response.form.FormResponse;
import yonyou.bpm.rest.response.historic.HistoricActivityInstanceResponse;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;
import yonyou.bpm.rest.response.historic.HistoricTaskInstanceResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author XingJiajun
 * @Date 2019/1/11 10:23
 * @Description
 */
@Service
public class BPMDataConvertService extends AbstractNCDataProcessService implements IBPMDataConvertService {

    private final NCProperties ncProperties;

    @Autowired
    public BPMDataConvertService(NCProperties ncProperties) {
        this.ncProperties = ncProperties;
    }


    @Override
    public JsonNode getNCDataNode(String jsonStr) {
        JsonNode ncDataNode = super.getNCDataNode(jsonStr);
        checkNCData(ncDataNode);
        return ncDataNode;
    }

    @Override
    public HistoricProcessInstanceResponse resolve2BpmApproveDetail(String jsonStr, NCBillDetailParams params) {
        NCTaskBillResponse ncTaskBillResponse = convertResponse(getNCDataNode(jsonStr), NCTaskBillResponse.class);
        if (ncTaskBillResponse == null) {
            return null;
        }
        return getHistoricProcessInstanceResponse(ncTaskBillResponse, params);
    }

    @Override
    public List<HistoricTaskInstanceResponse> resolve2BPMHisTasks(String jsonStr, NCBillDetailParams params) {
        NCApproveDetailResponse ncApproveDetailResponse = convertResponse(getNCDataNode(jsonStr), NCApproveDetailResponse.class);
        if (ncApproveDetailResponse == null) {
            return null;
        }
        return resolveHistoricTasks(null, ncApproveDetailResponse, null);
    }

    private void packHistoricProcessInstanceResponseWithNCApproveDetail(
            String taskId,
            String jsonStr,
            HistoricProcessInstanceResponse historicProcessInstanceResponse) {
        NCApproveDetailResponse ncApproveDetailResponse = convertResponse(getNCDataNode(jsonStr), NCApproveDetailResponse.class);
        if (ncApproveDetailResponse == null) {
            return;
        }
        resolveHistoricTasks(taskId, ncApproveDetailResponse, historicProcessInstanceResponse);
    }

    void packHistoricProcessInstanceResponseWithNCApproveDetail(
            String taskId, String groupId, String jsonStr,
            HistoricProcessInstanceResponse historicProcessInstanceResponse) {
        if (isNC63(groupId)) {
            packHistoricProcessInstanceResponseWithNCApproveDetail(taskId, jsonStr, historicProcessInstanceResponse);
            return;
        }

        NC65ApproveDetailResponse nc65ApproveDetailResponse = convertResponse(getNCDataNode(jsonStr), NC65ApproveDetailResponse.class);
        if (nc65ApproveDetailResponse == null) {
            return;
        }
        resolveHistoricTasks(taskId, nc65ApproveDetailResponse, historicProcessInstanceResponse);
    }

    @Override
    public List<Attachment> resolve2BPMAttachments(String jsonStr) {
        NCAttachmentResponse ncAttachmentResponse = convertResponse(getNCDataNode(jsonStr), NCAttachmentResponse.class);
        if (ncAttachmentResponse == null) {
            return null;
        }
        List<NCAttachGroupData> ncAttachGroupDataList = ncAttachmentResponse.getDataResult();
        if (ncAttachGroupDataList == null || ncAttachGroupDataList.size() == 0) {
            return null;
        }

        List<Attachment> rList = new ArrayList<>();
        for (NCAttachGroupData attachGroup : ncAttachGroupDataList) {
            List<NCAttachData> ncAttachDataList = attachGroup.getAttachmentgrouplist();
            if (ncAttachDataList == null || ncAttachDataList.size() == 0)
                continue;
            for (NCAttachData attachNCData : ncAttachDataList)
                rList.add(fromNCAttachData(attachNCData));
        }
        return rList;
    }

    @Override
    public List<Attachment> resolveAttachFromApproveDetail(String jsonStr) {
        NCApproveDetailResponse ncApproveDetailResponse = convertResponse(getNCDataNode(jsonStr), NCApproveDetailResponse.class);
        if (ncApproveDetailResponse == null) {
            return Collections.emptyList();
        }

        NCApproveHistoryDataAdapter approveHistoryDataAdapter = getApproveHistoryDataAdapter(ncApproveDetailResponse);
        if (approveHistoryDataAdapter == null) {
            return Collections.emptyList();
        }

        return resolveAttachFormHistoryList(approveHistoryDataAdapter.getApprovehistorylinelist());
    }

    List<Attachment> resolveAttachFromApproveDetail(String groupId, String jsonStr) {
        if (isNC63(groupId))
            return resolveAttachFromApproveDetail(jsonStr);

        NC65ApproveDetailResponse nc65ApproveDetailResponse = convertResponse(getNCDataNode(jsonStr), NC65ApproveDetailResponse.class);
        if (nc65ApproveDetailResponse == null) {
            return Collections.emptyList();
        }

        return resolveAttachFormHistoryList(nc65ApproveDetailResponse.getApprovehistorylinelist());

    }

    private List<Attachment> resolveAttachFormHistoryList(List<NCApproveHistoryData> approvehistorylinelist) {
        if (approvehistorylinelist == null || approvehistorylinelist.size() == 0) {
            return Collections.emptyList();
        }

        Set<NCAttachData> ncAttachData = new HashSet<>();
        for (NCApproveHistoryData historyData : approvehistorylinelist) {
            List<NCAttachGroupData> attachGroupDataList = historyData.getAttachstructlist();
            if (attachGroupDataList == null || attachGroupDataList.size() == 0)
                continue;
            NCAttachGroupData attachGroupData = attachGroupDataList.get(0);
            List<NCAttachData> attachList = attachGroupData.getAttachmentgrouplist();
            if (attachList == null || attachList.size() == 0)
                continue;
            ncAttachData.addAll(attachList);
        }

        return ncAttachData.stream()
                .map(this::fromNCAttachData)
                .collect(Collectors.toList());
    }

    private Attachment fromNCAttachData(NCAttachData attachNCData) {
        Attachment attachment = new Attachment();
        attachment.setId(attachNCData.getFileid());
        attachment.setName(attachNCData.getFilename());
        attachment.setAuthor("NC附件");
        int extIndex = attachNCData.getFilename().lastIndexOf(".");
        if (extIndex != -1) {
            attachment.setType(attachNCData.getFilename().substring(
                    extIndex + 1));
        } else {
            attachment.setType("unknown");
        }
        return attachment;
    }

    @Override
    public byte[] resolveNCFileData(String jsonStr) {
        NCFileDataResponse fileDataResponse = convertResponse(getNCDataNode(jsonStr), NCFileDataResponse.class);
        if (fileDataResponse == null) {
            return null;
        }
        String downloaded = fileDataResponse.getDownloaded();
        if (StringUtils.isBlank(downloaded)) {
            return null;
        }
        return new FileDataConvector().getByteArrayFromBase64String(downloaded);
    }

    private HistoricProcessInstanceResponse getHistoricProcessInstanceResponse(NCTaskBillResponse ncTaskBillResponse,
                                                                               NCBillDetailParams params) {
        HistoricProcessInstanceResponse detail = new HistoricProcessInstanceResponse();
        detail.setId(params.getTaskId());
        List<HistoricTaskInstanceResponse> historicTasks = new ArrayList<>();
        HistoricTaskInstanceResponse historicTaskInstanceResponse = new HistoricTaskInstanceResponse();
        historicTaskInstanceResponse.setAssignee(params.getUserid());
        historicTaskInstanceResponse.setId(params.getTaskId());
        historicTasks.add(historicTaskInstanceResponse);
        detail.setHistoricTasks((historicTasks));

        List<HistoricActivityInstanceResponse> historicActivities = new ArrayList<>();
        HistoricActivityInstanceResponse historicActivityInstanceResponse = new HistoricActivityInstanceResponse();
        historicActivities.add(historicActivityInstanceResponse);
        detail.setHistoricActivityInstances(historicActivities);

        NCBillData ncBillData = ncTaskBillResponse.getDataResult();

        List<Map<String, Object>> formDataList = new ArrayList<>();
        Map<String, Object> formDataMap = new HashMap<>();
        formDataList.add(formDataMap);
        detail.setFormDataList(formDataList);
        List<FormResponse> bpmForms = new ArrayList<>();
        detail.setBpmForms(bpmForms);

        FormResponse formResponse = new FormResponse();
        bpmForms.add(formResponse);
        List<FormFieldResponse> headFields = new ArrayList<>();
        formResponse.setFields(headFields);
        BillHead head = ncBillData.getHead();

        if (head != null) {
            List<BillHeadTab> headTabs = head.getBillTabs();
            if (headTabs != null) {
                for (BillHeadTab headTab : headTabs) {
                    String tabCode = headTab.getCode();
                    String tabName = ncTaskBillResponse.getBilltypename();
                    detail.setName(tabName);
                    formResponse.setTitle(tabName);
                    formResponse.setDescription("");
                    BillHeadContent headContent = headTab.getContent();
                    if (headContent != null
                            && headContent.getItems() != null) {
                        List<BillItem> billItems = headContent.getItems();
                        detail.setStartTime(getBillDate(billItems));
                        List<FormFieldResponse> fields = getFieldsFromBillItems(
                                tabCode, billItems);
                        if (fields != null) {
                            headFields.addAll(fields);
                        }
                        fillFormDataMap(tabCode, billItems, formDataMap);
                    }
                }
            }
        }// head end

        BillTail tail = ncBillData.getTail();
        // tail begin
        if (tail != null) {
            List<BillTailTab> tailTabs = tail.getBillTailTab();
            if (tailTabs != null) {
                for (BillTailTab tailTab : tailTabs) {
                    String tabCode = tailTab.getCode();
                    BillTailContent tailContent = tailTab.getContent();
                    if (tailContent != null
                            && tailContent.getItems() != null) {
                        List<BillItem> billItems = tailContent.getItems();
                        List<FormFieldResponse> fields = getFieldsFromBillItems(
                                tabCode, billItems);
                        if (fields != null) {
                            headFields.addAll(fields);
                        }
                        fillFormDataMap(tabCode, billItems, formDataMap);
                    }
                }
            }
        }// tail end

        // body begin
        BillBody body = ncBillData.getBody();
        if (body != null) {
            List<BillBodyTab> bodyTabs = body.getBillBodyTabs();
            if (bodyTabs != null) {
                List<FormResponse> subFormResponses = new ArrayList<>();
                Map<String, Object> subFormDataMap;
                for (BillBodyTab bodyTab : bodyTabs) {
                    String tabCode = bodyTab.getCode();
                    String tabName = bodyTab.getName();
                    List<BillBodyContent> bodyContents = bodyTab
                            .getContent();
                    List<Map<String, Object>> subFormDataMapList;

                    int count = 0;// 子表计数 xingjjc
                    if (bodyContents != null && bodyContents.size() > 0) {
                        for (BillBodyContent bodyContent : bodyContents) {
                            if (bodyContent != null
                                    && bodyContent.getItems() != null) {
                                String tableCodeCount = tabCode + "_"
                                        + count++ + "_";// 每个子表的表名
                                subFormDataMapList = new ArrayList<>();
                                subFormDataMap = new HashMap<>();
                                subFormDataMapList.add(subFormDataMap);
                                formDataMap.put(tableCodeCount,
                                        subFormDataMapList);
                                FormResponseEx subFormResponse = new FormResponseEx();
                                subFormResponses.add(subFormResponse);
                                subFormResponse
                                        .setTableName(tableCodeCount);
                                subFormResponse.setTitle(tabName);
                                subFormResponse.setDescription("");
                                List<BillItem> billItems = bodyContent
                                        .getItems();
                                List<FormFieldResponse> fields = getFieldsFromBillItems(
                                        tableCodeCount, billItems);
                                subFormResponse.setFields(fields);
                                fillFormDataMap(tableCodeCount, billItems,
                                        subFormDataMap);
                            }
                        }
                    }
                }
                formResponse.setSubForms(subFormResponses);
            }
        }// body end

        return detail;
    }

    private Date getBillDate(List<BillItem> billItems) {
        String billDateFieldName = "billdate";
        if (billItems == null || billItems.size() == 0)
            return null;
        for (BillItem billItem : billItems) {
            if (billDateFieldName.equals(billItem.getSourceFieldName())) {

                return DateUtil.parseDate(billItem.getShowValue());
            }
        }
        return null;
    }

    private List<FormFieldResponse> getFieldsFromBillItems(String tabCode,
                                                           List<BillItem> billItems) {
        if (billItems == null || billItems.size() == 0)
            return null;
        List<FormFieldResponse> fields = new ArrayList<>();
        for (BillItem billItem : billItems) {
            FormFieldResponseEx field = new FormFieldResponseEx();
            field.setFieldContent(getFieldContent(tabCode, billItem));
            field.setVariableContent(getVariableContent(tabCode, billItem));
            field.setTableFieldName(getFieldId(tabCode, billItem).toUpperCase());// 改为大写
            fields.add(field);
        }
        return fields;
    }

    private String getFieldContent(String tabCode, BillItem billItem) {
        String id = getFieldId(tabCode, billItem);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> bindingMap = new HashMap<>();
        bindingMap.put("variableId", id);
        map.put("binding", bindingMap);
        map.put("id", id);
        map.put("code", id);
        return JsonResultService.toJson(map);
    }

    private String getFieldId(String tabCode, BillItem billItem) {
        return tabCode + billItem.getSourceFieldName();
    }

    private String getVariableContent(String tabCode, BillItem billItem) {
        String id = getFieldId(tabCode, billItem);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", billItem.getShowName());
        Map<String, Object> typeMap = new HashMap<>();
        typeMap.put("name", "String");// TODO nc的表单字段类型暂时都置为String
        typeMap.put("multiLine", false);
        map.put("type", typeMap);
        return JsonResultService.toJson(map);
    }

    private void fillFormDataMap(String tabCode, List<BillItem> billItems,
                                 Map<String, Object> formDataMap) {
        if (billItems == null || billItems.size() == 0)
            return;
        for (BillItem billItem : billItems) {
            fillFormDataMap(tabCode, billItem, formDataMap);
        }
    }

    private void fillFormDataMap(String tabCode, BillItem billItem,
                                 Map<String, Object> formDataMap) {
        String id = getFieldId(tabCode, billItem);
        formDataMap.put(id.toUpperCase(), billItem.getShowValue());
    }

    private List<HistoricTaskInstanceResponse> resolveHistoricTasks(String taskId,
                                                                    NCApproveDetailResponse approveDetailResponse,
                                                                    HistoricProcessInstanceResponse historicProcessInstanceResponse) {
        if (historicProcessInstanceResponse == null)
            return Collections.emptyList();

        NCApproveHistoryDataAdapter ncApproveHistoryDataAdapter = getApproveHistoryDataAdapter(approveDetailResponse);
        if (ncApproveHistoryDataAdapter == null) {
            return Collections.emptyList();
        }

        List<NCApproveHistoryData> approveHisList = ncApproveHistoryDataAdapter.getApprovehistorylinelist();
        if (approveHisList == null || approveHisList.size() == 0) {
            historicProcessInstanceResponse.setEndTime(deletedDate());
            historicProcessInstanceResponse.setDeleteReason("deleted");
            return Collections.emptyList();
        }

//        List<HistoricTaskInstanceResponse> historicTaskInstanceResponses = approveHisList.stream()
//                .map(this::buildHistoricTask)
//                .collect(Collectors.toList());

        List<HistoricTaskInstanceResponse> historicTaskInstanceResponses = new ArrayList<>(approveHisList.size());
        boolean hideButton = false;
        for (NCApproveHistoryData ncApproveHistoryData : approveHisList) {
            //隐藏按钮
            if (!hideButton &&
                    taskId.equals(ncApproveHistoryData.getApprovedid()) &&
                    StringUtils.equalsAny(String.valueOf(ncApproveHistoryData.getApprovestatus()), "4", "null")) {
                hideButton = true;
                historicProcessInstanceResponse.setEndTime(hiddenDate());
            }

            historicTaskInstanceResponses.add(buildHistoricTask(ncApproveHistoryData));
        }

        historicProcessInstanceResponse.setHistoricTasks(historicTaskInstanceResponses);
        historicProcessInstanceResponse.setHistoricActivityInstances(
                resolveHistoricActivities(
                        approveDetailResponse.getMakername(), approveDetailResponse.getSubmitdate()));
        List<NCFlowHistoryData> flowHistoryDataList = ncApproveHistoryDataAdapter.getFlowhistory();
        if (flowHistoryDataList != null && flowHistoryDataList.size() > 0) {
            flowHistoryDataList.stream()
                    .filter(x -> "final".equalsIgnoreCase(x.getUnittype()))
                    .findAny()
                    .ifPresent(x -> {
                        historicProcessInstanceResponse.setDeleteReason("end");
                        historicProcessInstanceResponse.setEndTime(x.getTime());
                    });
        }

        return historicTaskInstanceResponses;
    }

    private void resolveHistoricTasks(String taskId,
                                      NC65ApproveDetailResponse approveDetailResponse,
                                      HistoricProcessInstanceResponse historicProcessInstanceResponse) {
        if (historicProcessInstanceResponse == null)
            return;

        List<NCApproveHistoryData> approveHisList = approveDetailResponse.getApprovehistorylinelist();

        //收回的任务审批历史为空，不显示按钮，状态显示已收回
        if (approveHisList == null || approveHisList.size() == 0) {
            historicProcessInstanceResponse.setEndTime(deletedDate());
            historicProcessInstanceResponse.setDeleteReason("deleted");
            return;
        }

        List<HistoricTaskInstanceResponse> historicTaskInstanceResponses = new ArrayList<>(approveHisList.size());
//                approveHisList.stream()
//                .map(this::buildHistoricTask)
//                .collect(Collectors.toList());
        boolean hideButton = false;
        for (NCApproveHistoryData ncApproveHistoryData : approveHisList) {
            historicTaskInstanceResponses.add(buildHistoricTask(ncApproveHistoryData));
            //根据当前任务approvestatus隐藏按钮
            if (!hideButton &&
                    taskId.equals(ncApproveHistoryData.getApprovedid()) &&
                    StringUtils.equalsAny(String.valueOf(ncApproveHistoryData.getApprovestatus()), "4", "null")) {
                hideButton = true;
                historicProcessInstanceResponse.setEndTime(hiddenDate());
            }
        }

        historicProcessInstanceResponse.setHistoricTasks(historicTaskInstanceResponses);
        List<NCFlowHistoryData> flowHistoryDataList = approveDetailResponse.getFlowhistory();
        if (flowHistoryDataList != null && flowHistoryDataList.size() > 0) {
            flowHistoryDataList.stream()
                    .filter(x -> "final".equalsIgnoreCase(x.getUnittype()))
                    .findAny()
                    .ifPresent(x -> {
                        historicProcessInstanceResponse.setDeleteReason("end");
                        historicProcessInstanceResponse.setEndTime(x.getTime());
                    });
            Map<String, List<NCFlowHistoryData>> collect = flowHistoryDataList.stream()
                    .collect(Collectors.groupingBy(NCFlowHistoryData::getUnittype));
            if (collect.containsKey("final")) {
                historicProcessInstanceResponse.setDeleteReason("end");
                historicProcessInstanceResponse.setEndTime(collect.get("final").get(0).getTime());
            }
            if (collect.containsKey("submit")) {
                NCFlowHistoryData submit = collect.get("submit").get(0);
                historicProcessInstanceResponse.setHistoricActivityInstances(
                        resolveHistoricActivities(
                                submit.getPersonlist().get(0).getName(), submit.getTime()));
            }
        }

    }

    private NCApproveHistoryDataAdapter getApproveHistoryDataAdapter(NCApproveDetailResponse approveDetailResponse) {
        List<NCApproveHistoryDataAdapter> approvehistorylinelist = approveDetailResponse.getApprovehistorylinelist();
        if (approvehistorylinelist == null || approvehistorylinelist.size() == 0) {
            return null;
        }
        return approvehistorylinelist.get(0);
    }

    private HistoricTaskInstanceResponse buildHistoricTask(NCApproveHistoryData ncApproveHistoryData) {
        HistoricTaskInstanceResponseEx tsk = new HistoricTaskInstanceResponseEx();
        String tskId = ncApproveHistoryData.getApprovedid();
        Date entTime = Optional.ofNullable(ncApproveHistoryData.getHandledate())
                .map(DateSwap::new)
                .orElse(null);
        tsk.setId(tskId);
        tsk.setEndTime(entTime);
        tsk.setAssignee(ncApproveHistoryData.getPsnid());
        tsk.setUserName(ncApproveHistoryData.getHandlername());
        tsk.setDeleteReason(ncApproveHistoryData.getAction());
        tsk.setDescription(ncApproveHistoryData.getAction());
        String note = ncApproveHistoryData.getNote();
        if (StringUtils.isNotEmpty(note)) {
            List<CommentResponse> taskComments = new ArrayList<>(1);
            CommentResponse taskComment = new CommentResponse();
            taskComment.setMessage(note);
            taskComment.setTime(entTime);
            taskComment.setTaskId(tskId);
            taskComments.add(taskComment);
            tsk.setTaskComments(taskComments);
        }
        return tsk;
    }

    private List<HistoricActivityInstanceResponse> resolveHistoricActivities(
            String userName, Date submitDate) {
        List<HistoricActivityInstanceResponse> historicActivityInstanceResponses = new ArrayList<>(1);
        // 制单环节
        HistoricActivityInstanceResponseEx historicActivityInstanceResponseEx = new HistoricActivityInstanceResponseEx();
        historicActivityInstanceResponseEx.setActivityType("startEvent");
        historicActivityInstanceResponseEx.setActivityName("制单");
        historicActivityInstanceResponseEx.setUserName(userName);
        historicActivityInstanceResponseEx.setAssignee(userName);
        historicActivityInstanceResponseEx.setEndTime(new DateSwap(submitDate));
        historicActivityInstanceResponses
                .add(historicActivityInstanceResponseEx);

        // TODO 其他环节...

        return historicActivityInstanceResponses;
    }

    private <T extends NCBaseResponse> T convertResponse(JsonNode jsonNode, Class<T> tClass) {
        return new NCResponseWrapper(jsonNode).wrap(tClass);
    }

    private boolean isNC63(String groupId) {
        return ncProperties.getGroupid().equals(groupId);
    }

    //标志按钮隐藏的时间 1980-01-01 08:00:00
    private Date hiddenDate() {
        return DateUtils.addYears(deletedDate(), 10);
    }

    //标志任务收回删除态的时间 1970-01-01 08:00:00
    private Date deletedDate() {
        return Date.from(Instant.EPOCH);
    }

}
