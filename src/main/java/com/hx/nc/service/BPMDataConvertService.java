package com.hx.nc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hx.nc.bo.nc.NCBillDetailParams;
import com.hx.nc.data.bill.*;
import com.hx.nc.data.bpm.*;
import com.hx.nc.data.convert.DateSwap;
import com.hx.nc.data.wrap.*;
import com.hx.nc.data.wrap.response.*;
import org.springframework.stereotype.Service;
import yonyou.bpm.rest.ex.util.DateUtil;
import yonyou.bpm.rest.response.CommentResponse;
import yonyou.bpm.rest.response.form.FormFieldResponse;
import yonyou.bpm.rest.response.form.FormResponse;
import yonyou.bpm.rest.response.historic.HistoricActivityInstanceResponse;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;
import yonyou.bpm.rest.response.historic.HistoricTaskInstanceResponse;

import java.util.*;

/**
 * @author XingJiajun
 * @Date 2019/1/11 10:23
 * @Description
 */
@Service
public class BPMDataConvertService extends AbstractNCDataProcessService implements IBPMDataConvertService {

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
        return getHistoricTasks(ncApproveDetailResponse, params, null);
    }

    public void packHistoricProcessInstanceResponseWithNCApproveDetail(
            String jsonStr, NCBillDetailParams params,
            HistoricProcessInstanceResponse historicProcessInstanceResponse) {
        NCApproveDetailResponse ncApproveDetailResponse = convertResponse(getNCDataNode(jsonStr), NCApproveDetailResponse.class);
        if (ncApproveDetailResponse == null) {
            return;
        }
        getHistoricTasks(ncApproveDetailResponse, params, historicProcessInstanceResponse);
    }

    @Override
    public List<Attachment> resolve2BPMAttachments(String jsonStr) {
        NCAttachmentResponse ncAttachmentResponse = convertResponse(getNCDataNode(jsonStr), NCAttachmentResponse.class);
        if (ncAttachmentResponse == null) {
            return null;
        }
        List<NCAttachData> ncAttachDataList = ncAttachmentResponse.getDataResult();
        if (ncAttachDataList == null || ncAttachDataList.size() == 0) {
            return null;
        }
        List<Attachment> rList = new ArrayList<>();
        Attachment attachment;
        for (NCAttachData attachNCData : ncAttachDataList) {
            attachment = new Attachment();
            rList.add(attachment);
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
        }
        return rList;
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

    private List<HistoricTaskInstanceResponse> getHistoricTasks(NCApproveDetailResponse approveDetailResponse,
                                                                NCBillDetailParams params,
                                                                HistoricProcessInstanceResponse historicProcessInstanceResponse) {
        NCApproveHistoryDataAdapter ncApproveHistoryDataAdapter =
                approveDetailResponse.getApprovehistorylinelist().get(0);
        String billNum = approveDetailResponse.getBillname();
        List<HistoricTaskInstanceResponse> htis = new ArrayList<>();
        HistoricTaskInstanceResponseEx htir;
        List<NCApproveHistoryData> ahlines = ncApproveHistoryDataAdapter.getApprovehistorylinelist();
        NCApproveHistoryData userUnhandledTask = getNcUserTask(params.getUserid(), params.getTaskId(), ahlines);
        List<NCFlowHistoryData> flowhistories = ncApproveHistoryDataAdapter.getFlowhistory();

        if (ahlines != null) {
            List<CommentResponse> taskComments;
            CommentResponse taskComment;
            Date dt;
            for (NCApproveHistoryData ahline : ahlines) {
                if (ahline.getHandledate() == null)
                    continue;
                taskComments = new ArrayList<>();
                htir = new HistoricTaskInstanceResponseEx();
                htis.add(htir);
                htir.setAssignee(ahline.getHandlername());
                htir.setDeleteReason(ahline.getAction());// TODO:完成方式
                dt = ahline.getHandledate();
                if (dt != null) {
                    htir.setEndTime(new DateSwap(dt));// 完成时间
                }
                htir.setFormKey(billNum);
                htir.setUserName(ahline.getHandlername());
                if (ahline.getNote() != null && !"".equals(ahline.getNote())) {
                    taskComment = new CommentResponse();
                    taskComment.setMessage(ahline.getNote());
                    taskComment.setTime(new DateSwap(dt));
                    taskComment.setTaskId(ahline.getApprovedid());
                    taskComments.add(taskComment);
                }
                htir.setTaskComments(taskComments);
            }
        }

        if (flowhistories != null) {
            // 待办节点
            if (userUnhandledTask != null
                    && userUnhandledTask.getHandledate() == null) {
                htir = new HistoricTaskInstanceResponseEx();
                htis.add(htir);
                htir.setAssignee(params.getUserid());
                htir.setId(params.getTaskId());
                htir.setUserName(userUnhandledTask.getHandlername());// 暂时写死
            }
        }

        if (historicProcessInstanceResponse != null) {
            historicProcessInstanceResponse.setHistoricTasks(htis);
            historicProcessInstanceResponse.setHistoricActivityInstances(getHistoricActivities(approveDetailResponse));
        }

        return htis;
    }

    private List<HistoricActivityInstanceResponse> getHistoricActivities(
            NCApproveDetailResponse ncApprovedDetail) {
        List<HistoricActivityInstanceResponse> historicActivityInstanceResponses = new ArrayList<>(1);

        // 制单环节
        HistoricActivityInstanceResponseEx historicActivityInstanceResponseEx = new HistoricActivityInstanceResponseEx();
        historicActivityInstanceResponseEx.setActivityType("startEvent");
        historicActivityInstanceResponseEx.setActivityName("制单");
        historicActivityInstanceResponseEx.setUserName(ncApprovedDetail
                .getMakername());
        historicActivityInstanceResponseEx.setAssignee(ncApprovedDetail
                .getMakername());
        historicActivityInstanceResponseEx.setEndTime(new DateSwap(
                ncApprovedDetail.getSubmitdate()));
        historicActivityInstanceResponses
                .add(historicActivityInstanceResponseEx);

        // TODO 其他环节...

        return historicActivityInstanceResponses;
    }

    private NCApproveHistoryData getNcUserTask(String ncUser, String taskId, List<NCApproveHistoryData> ahlines) {
        if (ahlines == null || ahlines.size() == 0)
            return null;
        for (NCApproveHistoryData ahline : ahlines) {
            if (ncUser.equals(ahline.getPsnid())
                    && taskId.equals(ahline.getApprovedid()))
                return ahline;
        }
        return null;
    }

    private <T extends NCBaseResponse> T convertResponse(JsonNode jsonNode, Class<T> tClass) {
        return new NCResponseWrapper(jsonNode).wrap(tClass);
    }

}
