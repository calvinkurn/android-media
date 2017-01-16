package com.tokopedia.inbox.rescenter.detail.facade;

import android.net.Uri;

import com.tokopedia.inbox.rescenter.detail.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.detail.model.passdata.ActivityParamenterPassData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hangnadi on 2/9/16.
 */
public class NetworkParam {

    private static final int STATIC_GOLANG_VALUE = 2;
    private static final String PARAM_RESOLUTION_ID = "resolution_id";
    private static final String PARAM_HAS_SOLUTION = "has_solution";
    private static final String PARAM_START_UT = "start_ut";
    private static final String PARAM_LAST_UT = "last_ut";
    private static final String PARAM_EDIT_SOL_FLAG = "edit_solution_flag";
    private static final String PARAM_FLAG_RECEIVED = "flag_received";
    private static final String PARAM_PHOTOS = "photos";
    private static final String PARAM_REFUND_AMOUNT = "refund_amount";
    private static final String PARAM_REPLY_MSG = "reply_msg";
    private static final String PARAM_SOLUTION = "solution";
    private static final String PARAM_TROUBLE_TYPE = "trouble_type";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";
    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_SERVER_LANGUAGE = "new_add";
    private static final String PARAM_ATTACHMENT_STRING = "attachment_string";
    private static final String PARAM_FILE_PATH = "file_path";
    private static final String PARAM_SERVER_ID = "server_id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_SHIPPING_REF = "shipping_ref";
    private static final String PARAM_SHIPMENT_ID = "shipment_id";
    private static final String PARAM_CONVERSATION_ID = "conversation_id";

    public static Map<String, String> paramResCenterDetail(ActivityParamenterPassData activityParamenterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, activityParamenterPassData.getResCenterId());
        params.put("web_view", String.valueOf(1));
        return params;
    }

    public static Map<String, String> replyConversationValidation(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_EDIT_SOL_FLAG, String.valueOf(actionParameterPassData.getEdiSolFlag()));
        params.put(PARAM_FLAG_RECEIVED, String.valueOf(actionParameterPassData.getFlagReceived()));
        if (!(actionParameterPassData.getPhotos() == null || actionParameterPassData.getPhotos().isEmpty())) {
            params.put(PARAM_PHOTOS, actionParameterPassData.getPhotos());
        }
        params.put(PARAM_REFUND_AMOUNT, String.valueOf(actionParameterPassData.getRefundAmount()));
        params.put(PARAM_REPLY_MSG, actionParameterPassData.getReplyMsg());
        params.put(PARAM_RESOLUTION_ID, actionParameterPassData.getResolutionID());
        if (!(actionParameterPassData.getServerID() ==  null || actionParameterPassData.getServerID().isEmpty())) {
            params.put(PARAM_SERVER_ID, actionParameterPassData.getServerID());
        }
        params.put(PARAM_SOLUTION, String.valueOf(actionParameterPassData.getSolutionState()));
        params.put(PARAM_TROUBLE_TYPE, String.valueOf(actionParameterPassData.getTroubleState()));
        return params;
    }

    public static Map<String, String> generateHost() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_LANGUAGE, String.valueOf(STATIC_GOLANG_VALUE));
        return params;
    }

    public static Map<String, String> createResolutionPicture(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, actionParameterPassData.getResolutionID());
        params.put(PARAM_ATTACHMENT_STRING, actionParameterPassData.getAttachmentString());
        params.put(PARAM_FILE_PATH, actionParameterPassData.getFilePath());
        params.put(PARAM_SERVER_ID, actionParameterPassData.getServerID());
        params.put(PARAM_WEB_SERVICE, String.valueOf(1));
        return params;
    }

    public static Map<String, String> replyConversationSubmit(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_FILE_UPLOADED, actionParameterPassData.getFileUploaded());
        params.put(PARAM_POST_KEY, actionParameterPassData.getPostKey());
        params.put(PARAM_RESOLUTION_ID, actionParameterPassData.getResolutionID());
        return params;
    }

    public static Map<String, String> paramTrackingDelivery(String url) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SHIPMENT_ID, Uri.parse(url).getQueryParameter("kurir"));
        params.put(PARAM_SHIPPING_REF, Uri.parse(url).getQueryParameter("ship_ref"));
        return params;
    }

    public static Map<String, String> acceptAdminSolution(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, actionParameterPassData.getResolutionID());
        return params;
    }

    public static Map<String, String> finishReturSolution(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, actionParameterPassData.getResolutionID());
        return params;
    }

    public static Map<String, String> acceptSolution(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, actionParameterPassData.getResolutionID());
        return params;
    }

    public static Map<String, String> cancelResolution(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, actionParameterPassData.getResolutionID());
        return params;
    }

    public static Map<String, String> reportResolution(ActionParameterPassData actionParameterPassData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, actionParameterPassData.getResolutionID());
        return params;
    }

    public static Map<String, String> getParamInputEditAddress(String addressID, String ahrefEditAddressURL, String resolutionID) {
        String convID = Uri.parse(ahrefEditAddressURL).getQueryParameter("conv_id");
        String oldAddressID = Uri.parse(ahrefEditAddressURL).getQueryParameter("addr_id");
        Map<String, String> params = new HashMap<>();
        params.put("address_id", addressID);
        params.put("resolution_id", resolutionID);
        params.put("old_data", oldAddressID + "-" + convID);
        return params;
    }

    public static Map<String, String> getParamInputAddress(String addressID, String resolutionID) {
        Map<String, String> params = new HashMap<>();
        params.put("address_id", addressID);
        params.put("bypass", String.valueOf(1));
        params.put("resolution_id", resolutionID);
        params.put("new_address", String.valueOf(1));
        return params;
    }

    public static Map<String, String> getParamInputAddressMigrateVersion(String addressID, String resolutionID) {
        Map<String, String> params = new HashMap<>();
        params.put("address_id", addressID);
        params.put("resolution_id", resolutionID);
        params.put("new_address", String.valueOf(1));
        return params;
    }
}
