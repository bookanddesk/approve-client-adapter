package com.hx.nc.bo;

/**
 * @author XingJiajun
 * @Date 2018/12/12 14:59
 * @Description
 */
public class Constants {

    private Constants() {
    }

    public static final String ACA = "aca";

    public static final String UNDERLINE = "_";

    public static final String ZERO_STRING_VALUE = "0";
    public static final String ONE_STRING_VALUE = "1";
    public static final String TWO_STRING_VALUE = "2";
    public static final String THREE_STRING_VALUE = "3";

    public static final String NC_SERVLET_TASK = "/servlet/ncTaskList";
    public static final String NC_SERVLET_BILL_DETAIL = "/servlet/ncTaskBill";
    public static final String NC_SERVLET_APPROVE_DETAIL = "/servlet/ncApproveDetail";
    public static final String NC_SERVLET_ACTION = "/servlet/ncExeAction";
    public static final String NC_SERVLET_ATTACH_LIST = "/servlet/ncAttachList";
    public static final String NC_SERVLET_FILE_DATA = "/servlet/ncAttachFile";
    public static final String NC_SERVLET_ASSIGN_USER_LIST = "/servlet/ncAssignUserList";

    public static final String NC_PARAM_USER_ID = "userid";
    public static final String NC_PARAM_GROUP_ID = "groupid";
    public static final String NC_PARAM_LAST_DATE = "lastDate";
    public static final String NC_PARAM_TASK_ID = "taskId";
    public static final String NC_PARAM_FILE_ID = "fileid";
    public static final String NC_PARAM_DONE_TASK_QUERY_PARAM = "statuscode=handled";
    public static final String NC_PARAM_BILL_ID = "billId";
    public static final String NC_PARAM_PK_BILL_TYPE = "pk_billtype";
    public static final String NC_PARAM_BILL_TYPE = "billtype";
    public static final String NC_PARAM_ACTION = "action";
//    public static final String NC_PARAM_BILL_TYPE_NAME = "billtypename";
    public static final String NC_PARAM_ACTIONS_AGREE = "agree";
    public static final String NC_PARAM_APPROVE_MESSAGE = "approveMessage";
    public static final String NC_PARAM_C_USER_IDS = "cuserids";
    public static final String NC_DETAIL_URL_MOBILE = "/approve-client-adapter/index.html#detail?";

    public static final String NC_RESPONSE_PROP_TASK_STRUCT_LIST = "taskstructlist";
    public static final String NC_RESPONSE_FLAG = "flag";
    public static final String NC_RESPONSE_DES = "des";

    public static final String OA_REST_HEADER_TOKEN = "token";
    public static final String OA_REST_URI_TOKEN = "/seeyon/rest/token/{restusername}/{password}";
    public static final String OA_REST_URI_RECEIVE_PENDING = "/seeyon/rest/thirdpartyPending/receive/pendings";
    public static final String OA_REST_URI_UPDATE_PENDING = "/seeyon/rest/thirdpartyPending/updatePendingState";
    public static final String OA_REST_URI_UPDATE_USER_INFO = "/seeyon/rest/TestResource/testNewRest";
    public static final String OA_REST_RESPONSE_PROP_SUCCESS = "success";
    public static final String OA_REST_RESPONSE_ERROR_MSG = "errorMsgs";

    public static final String LAST_POLL_DATE_TIME = "lastPollDateTime";
    public static final String LAST_DONE_TASK_POLL_DATE_TIME = "lastDoneTaskPollDateTime";
    public static final long LAST_POLL_DURATION_MILLIS = 2 * 60 * 1_000;//2分钟
    public static final long LAST_POLL_DURATION_MINUTES = 2;//2分钟
    public static final long POLL_DELAY_ONE_MINUTES = 60 * 1_000;//1分钟
    public static final long POLL_DELAY_TWO_MINUTES = 2 * 60 * 1_000;//2分钟

    public static final String NC_DATA_FIELD_SUFFIX_ITEM_SHOW_NAME = "itemShowName";
    public static final String NC_DATA_FIELD_SUFFIX_ID = "_ID";
    public static final int NC_DATA_FIELD_SUFFIX_ITEM_SHOW_NAME_LENGTH = 12;
    public static final String NC_DATA_FIELD_SHOW_NAME = "showName";
    public static final String NC_DATA_FIELD_SHOW_VALUE = "showValue";
    public static final String NC_DATA_FIELD_SHOW_VALUE_ID = "showValueId";

    public static final String ACA_METRICS_OA_TASKS = "oaTasks";
    public static final String ACA_METRICS_ACTIONS = "actions";

    public static final String PARAM_PAGE = "page";
    public static final String PARAM_PAGE_SIZE = "size";

    public static final String CONTENT_DISPOSITION_ATTACHMENT = "attachment";
    public static final String CONTENT_DISPOSITION_TYPE_FORM_DATA = "attachment";


}
