package com.tokopedia.inbox.rescenter.edit.facade;

import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.edit.model.responsedata.PassProductTrouble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 8/25/16.
 */
public class NetworkParam {

    private static final String PARAM_CATEGORY_TROUBLE_ID = "category_trouble_id";
    private static final String PARAM_TROUBLE_ID = "trouble_id";
    private static final String PARAM_RESOLUTION_ID = "resolution_id";
    private static final String PARAM_ORDER_ID = "order_id";
    private static final String PARAM_STATUS_RECEIVED = "n";

    public static Map<String, String> paramEditResCenter(DetailResCenterData detailData, int received) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, detailData.getDetail().getResolutionLast().getLastResolutionId());
        params.put(PARAM_ORDER_ID, detailData.getDetail().getResolutionOrder().getOrderId());
        params.put(PARAM_STATUS_RECEIVED, String.valueOf(received));
        return params;
    }

    public static Map<String, String> getSolutionNonProductRelatedParam(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put("solution_forms", genereateJsonObjectNonProductRelated(passData));
        return params;
    }

    private static String genereateJsonObjectNonProductRelated(ActionParameterPassData passData) {
        try {
            JSONObject result = new JSONObject();
            result.put(PARAM_CATEGORY_TROUBLE_ID, Integer.parseInt(passData.getTroubleCategoryChoosen().getCategoryTroubleId()));
            result.put(PARAM_ORDER_ID, passData.getDetailData().getDetail().getResolutionOrder().getOrderId());
            result.put(PARAM_TROUBLE_ID, Integer.parseInt(passData.getTroubleChoosen().getTroubleId()));
            return result.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> getSolutionParam(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put("solution_forms", genereateJsonObject(passData));
        return params;
    }

    private static String genereateJsonObject(ActionParameterPassData passData) {
        try {
            JSONObject result = new JSONObject();
            result.put(PARAM_CATEGORY_TROUBLE_ID, Integer.parseInt(passData.getTroubleCategoryChoosen().getCategoryTroubleId()));
            result.put(PARAM_ORDER_ID, passData.getDetailData().getDetail().getResolutionOrder().getOrderId());
            JSONArray jsonArray = new JSONArray();
            for (PassProductTrouble var : passData.getProductTroubleChoosenList()) {
                JSONObject object = new JSONObject();
                object.put("product_id", Integer.parseInt(var.getProductData().getProductId()));
                object.put("trouble_id", Integer.parseInt(var.getTroubleData().getTroubleId()));
                object.put("quantity", var.getInputQuantity());
                object.put("order_dtl_id", Integer.parseInt(var.getProductData().getOrderDetailId()));
                jsonArray.put(object);
            }
            result.put("product_list", jsonArray);
            return result.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String PARAM_SERVER_LANGUAGE = "new_add";
    private static final int STATIC_GOLANG_VALUE = 2;

    public static Map<String, String> generateHost() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_LANGUAGE, String.valueOf(STATIC_GOLANG_VALUE));
        return params;
    }

    private static final String PARAM_EDIT_SOL_FLAG = "edit_sol_flag";
    private static final String PARAM_ACTION_BY = "action_by";
    private static final String PARAM_FLAG_RECEIVED = "flag_received";
    private static final String PARAM_PHOTOS = "photos";
    private static final String PARAM_REFUND_AMOUNT = "refund_amount";
    private static final String PARAM_REPLY_MSG = "reply_msg";
    private static final String PARAM_SOLUTION = "solution";
    private static final String PARAM_REMARK = "remark";
    private static final String PARAM_TROUBLE_TYPE = "trouble_id";
    private static final String PARAM_SERVER_ID = "server_id";

    public static Map<String, String> paramEditResCenterValidation(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, passData.getResolutionID());
        params.put(PARAM_ACTION_BY, passData.getDetailData().getDetail().getResolutionBy().getByCustomer() == 1 ? "1" : "2");
        if (!(passData.getAttachmentString() == null || passData.getAttachmentString().isEmpty())) {
            params.put(PARAM_PHOTOS, passData.getAttachmentString());
        }
        params.put(PARAM_REFUND_AMOUNT, String.valueOf(passData.getRefund()));
        params.put(PARAM_EDIT_SOL_FLAG, String.valueOf(1));
        if (!(passData.getServerID() == null || passData.getServerID().isEmpty())) {
            params.put(PARAM_SERVER_ID, passData.getServerID());
        }
        params.put(PARAM_SOLUTION, String.valueOf(passData.getSolutionChoosen().getSolutionId()));
        params.put(PARAM_FLAG_RECEIVED, String.valueOf(passData.getPackageStatus()));
        params.put(PARAM_CATEGORY_TROUBLE_ID, passData.getTroubleCategoryChoosen().getCategoryTroubleId());
        if (passData.getProductTroubleChoosenList() == null || passData.getProductTroubleChoosenList().isEmpty()) {
            params.put(PARAM_REMARK, passData.getInputDescription());
            params.put(PARAM_TROUBLE_TYPE, passData.getTroubleChoosen().getTroubleId());
        } else {
            params.put("product_list", generateProductJsonObject(passData));
        }
        params.put(PARAM_REPLY_MSG, passData.getReplyMsg());
        return params;
    }

    private static String generateProductJsonObject(ActionParameterPassData passData) {
        try {
            JSONObject result = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (PassProductTrouble var : passData.getProductTroubleChoosenList()) {
                JSONObject object = new JSONObject();
                object.put("trouble_id", var.getTroubleData().getTroubleId());
                object.put("product_id", var.getProductData().getProductId());
                object.put("remark", var.getInputDescription());
                object.put("quantity", var.getInputQuantity());
                object.put("order_dtl_id", var.getProductData().getOrderDetailId());
                jsonArray.put(object);
            }
            result.put("data", jsonArray);
            return result.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";

    public static Map<String, String> paramEditResCenterSubmit(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, passData.getResolutionID());
        params.put(PARAM_POST_KEY, passData.getPostKey());
        params.put(PARAM_FILE_UPLOADED, passData.getFileUploaded());
        return params;
    }

    public static Map<String, String> paramAppealResCenter(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, passData.getResolutionID());
        return params;
    }

    public static Map<String, String> paramEditResCenterSellerValidation(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, passData.getResolutionID());
        params.put(PARAM_ACTION_BY, passData.getDetailData().getDetail().getResolutionBy().getByCustomer() == 1 ? "1" : "2");
        if (!(passData.getAttachmentString() == null || passData.getAttachmentString().isEmpty())) {
            params.put(PARAM_PHOTOS, passData.getAttachmentString());
        }
        params.put(PARAM_REFUND_AMOUNT, String.valueOf(passData.getRefund()));
        params.put(PARAM_EDIT_SOL_FLAG, String.valueOf(1));
        if (!(passData.getServerID() == null || passData.getServerID().isEmpty())) {
            params.put(PARAM_SERVER_ID, passData.getServerID());
        }
        params.put(PARAM_SOLUTION, String.valueOf(passData.getSolutionChoosen().getSolutionId()));
        params.put(PARAM_REPLY_MSG, passData.getReplyMsg());
        return params;
    }

    public static Map<String, String> paramAppealResCenterValidation(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RESOLUTION_ID, passData.getResolutionID());
        params.put(PARAM_ACTION_BY, passData.getDetailData().getDetail().getResolutionBy().getByCustomer() == 1 ? "1" : "2");
        if (!(passData.getAttachmentString() == null || passData.getAttachmentString().isEmpty())) {
            params.put(PARAM_PHOTOS, passData.getAttachmentString());
        }
        params.put(PARAM_REFUND_AMOUNT, String.valueOf(passData.getRefund()));
        if (!(passData.getServerID() == null || passData.getServerID().isEmpty())) {
            params.put(PARAM_SERVER_ID, passData.getServerID());
        }
        params.put(PARAM_SOLUTION, String.valueOf(passData.getSolutionChoosen().getSolutionId()));
        params.put(PARAM_REPLY_MSG, passData.getReplyMsg());
        return params;
    }
}
