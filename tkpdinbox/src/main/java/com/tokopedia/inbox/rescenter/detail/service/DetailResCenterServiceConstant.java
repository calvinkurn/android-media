package com.tokopedia.inbox.rescenter.detail.service;

/**
 * Created by hangnadi on 2/22/16.
 */
public interface DetailResCenterServiceConstant {

    /**
     * constant bundle variable name
     */
    int DEFAULT_ACTION = 0;
    int ACTION_CHANGE_SOLUTION = 0x1;
    int ACTION_REPLY_CONVERSATION = 0x2;
    int ACTION_ACCEPT_ADMIN_SOLUTION = 0x5;
    int ACTION_ACCEPT_SOLUTION = 0x6;
    int ACTION_FINISH_RETUR_SOLUTION = 0x7;
    int ACTION_CANCEL_RESOLUTION = 0x8;
    int ACTION_REPORT_RESOLUTION = 0x9;
    String EXTRA_PARAM_ACTION_TYPE = "EXTRA_PARAM_ACTION_TYPE";
    String EXTRA_PARAM_RESPONSE_DATA = "EXTRA_PARAM_RESPONSE_DATA";
    String EXTRA_PARAM_NETWORK_ERROR_MESSAGE = "EXTRA_PARAM_NETWORK_ERROR_MESSAGE";
    String EXTRA_PARAM_NETWORK_ERROR_TYPE = "EXTRA_PARAM_NETWORK_ERROR_TYPE";

    /**
     * Error response
     */
    String MESSAGE_NETWORK_ERROR_CODE = "NETWORK_ERROR_CODE";
    String MESSAGE_SERVER_INFO = "Network Server Error";
    String MESSAGE_FORBIDDEN_INFO = "Network Forbidden";
    String MESSAGE_BAD_REQUEST_INFO = "Network Bad Request";
    String MESSAGE_UNKNOWN_INFO = "Network Error";
    String MESSAGE_TIMEOUT_INFO = "Network Timeout";
    int STATUS_TIME_OUT = 2;

    /**
     * status action
     */
    int STATUS_ERROR = 3;
    int STATUS_RUNNING = 4;
    int STATUS_FINISHED = 5;

}
