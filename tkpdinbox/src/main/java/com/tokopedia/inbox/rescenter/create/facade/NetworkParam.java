package com.tokopedia.inbox.rescenter.create.facade;

import com.tokopedia.inbox.rescenter.create.model.passdata.ActionParameterPassData;
import com.tokopedia.inbox.rescenter.create.model.passdata.PassProductTrouble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 6/20/16.
 */
public class NetworkParam {

    private static final String PARAM_CATEGORY_TROUBLE_ID = "category_trouble_id";
    private static final String PARAM_TROUBLE_ID = "trouble_id";
    private static final String PARAM_ORDER_ID = "order_id";

    // for action
    private static final String PARAM_FLAG_RECEIVED = "flag_received";
    private static final String PARAM_PHOTOS = "photos";
    private static final String PARAM_REFUND_AMOUNT = "refund_amount";
    private static final String PARAM_REMARK = "remark";
    private static final String PARAM_SERVER_ID = "server_id";
    private static final String PARAM_SOLUTION = "solution";
    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";
    private static final String PARAM_SERVER_LANGUAGE = "new_add";
    private static final int STATIC_GOLANG_VALUE = 2;

    public static Map<String, String> getFormCreateResCenter(ActionParameterPassData passData) {
        Map<String, String> param = new HashMap<>();
        param.put("order_id", passData.getOrderID());
        param.put("n", String.valueOf(passData.getFlagReceived()));
        if (passData.getFlagReceived() == 0) {
            param.put("t", String.valueOf(passData.getTroubleID()));
            param.put("s", String.valueOf(passData.getSolutionID()));
        }
        return param;
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
            result.put(PARAM_ORDER_ID, passData.getOrderID());
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

    public static Map<String, String> getSolutionNonProductRelatedParam(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put("solution_forms", genereateJsonObjectNonProductRelated(passData));
        return params;
    }

    private static String genereateJsonObjectNonProductRelated(ActionParameterPassData passData) {
        try {
            JSONObject result = new JSONObject();
            if (passData.getFlagReceived() == 0) {
                result.put("trouble_id", passData.getTroubleID());
                result.put("solution_id", passData.getSolutionID());
            }
            result.put(PARAM_CATEGORY_TROUBLE_ID, Integer.parseInt(passData.getTroubleCategoryChoosen().getCategoryTroubleId()));
            result.put(PARAM_ORDER_ID, passData.getOrderID());
            result.put(PARAM_TROUBLE_ID, Integer.parseInt(passData.getTroubleChoosen().getTroubleId()));
            return result.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> paramCreateResCenterValidation(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ORDER_ID, passData.getOrderID());
        if (!(passData.getAttachmentString() == null || passData.getAttachmentString().isEmpty())) {
            params.put(PARAM_PHOTOS, passData.getAttachmentString());
        }
        if (!(passData.getServerID() == null || passData.getServerID().isEmpty())) {
            params.put(PARAM_SERVER_ID, passData.getServerID());
        }
        params.put(PARAM_REFUND_AMOUNT, passData.getRefund());
        params.put(PARAM_SOLUTION, passData.getSolutionChoosen().getSolutionId());
        params.put(PARAM_FLAG_RECEIVED, String.valueOf(passData.getFlagReceived()));
        params.put(PARAM_CATEGORY_TROUBLE_ID, passData.getTroubleCategoryChoosen().getCategoryTroubleId());
        if (passData.getProductTroubleChoosenList() == null || passData.getProductTroubleChoosenList().isEmpty()) {
            params.put(PARAM_REMARK, passData.getInputDescription());
            params.put(PARAM_TROUBLE_ID, passData.getTroubleChoosen().getTroubleId());
        } else {
            params.put("product_list", generateProductJsonObject(passData));
        }
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

    public static Map<String, String> paramCreateResCenterSubmit(ActionParameterPassData passData) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ORDER_ID, passData.getOrderID());
        params.put(PARAM_POST_KEY, passData.getPostKey());
        params.put(PARAM_FILE_UPLOADED, passData.getFileUploaded());
        return params;
    }

    public static Map<String, String> generateHost() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_LANGUAGE, String.valueOf(STATIC_GOLANG_VALUE));
        return params;
    }
}
