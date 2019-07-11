package com.tokopedia.inbox.rescenter.shipping.interactor;

import com.tokopedia.core.database.model.ResCenterAttachment;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.shipping.model.ShippingParamsPostModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hangnadi on 12/14/16.
 */
public class NetworkParam {

    private static final int STATIC_GOLANG_VALUE = 2;
    private static final String PARAM_SERVER_LANGUAGE = "new_add";

    public static TKPDMapParam<String,String> getShippingListParams() {
        return new TKPDMapParam<>();
    }

    public static Map<String, String> generateHost() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SERVER_LANGUAGE, String.valueOf(STATIC_GOLANG_VALUE));
        return params;
    }

    public static TKPDMapParam<String, String> paramInputShippingValidation(ShippingParamsPostModel passData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("resolution_id", passData.getResolutionID());
        params.put("shipping_ref", passData.getShippingNumber());
        params.put("shipment_id", passData.getShippingID());
        if (passData.getAttachmentList() == null || passData.getAttachmentList().isEmpty()) {
            params.put("has_resi_photo", "0");
        } else {
            params.put("has_resi_photo", "1");
            params.put("resi_photo_all", getPhotos(passData.getAttachmentList()));
            params.put("resi_pic_obj", getPhotosObj(passData.getAttachmentList()));
        }
        return params;
    }

    private static String getPhotos(List<ResCenterAttachment> attachmentList) {
        if (attachmentList == null || attachmentList.isEmpty()) {
            return "";
        }

        String allPhoto = "";
        for (int i = 0; i < attachmentList.size(); i++) {
            if (i == attachmentList.size() - 1) {
                allPhoto += attachmentList.get(i).getImageUuid();
            } else {
                allPhoto += attachmentList.get(i).getImageUuid() + "~";
            }
        }

        return allPhoto;
    }

    private static String getPhotosObj(List<ResCenterAttachment> attachmentList) {

        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ResCenterAttachment image : attachmentList) {
                reviewPhotos.put(image.getImageUuid(), generateObject(image));
            }
            return reviewPhotos.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    private static JSONObject generateObject(ResCenterAttachment image) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image_id", image.getImageUuid());
        jsonObject.put("file_x", "80");
        jsonObject.put("file_y", "80");
        jsonObject.put("is_temp", "1");
        return jsonObject;
    }

    public static TKPDMapParam<String, String> paramInputShippingSubmit(ShippingParamsPostModel passData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("resolution_id", passData.getResolutionID());
        params.put("post_key", passData.getPostKey());
        params.put("file_uploaded", getFileUploaded(passData));
        return params;
    }

    private static String getFileUploaded(ShippingParamsPostModel passData) {
        try {
            JSONObject jsonObject = new JSONObject();
            for (ResCenterAttachment attachment: passData.getAttachmentList()) {
                jsonObject.put(attachment.getImageUuid(), attachment.getPicObj());
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    public static TKPDMapParam<String, String> paramEditShippingValidation(ShippingParamsPostModel passData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("resolution_id", passData.getResolutionID());
        params.put("conversation_id", passData.getConversationID());
        params.put("shipping_ref", passData.getShippingNumber());
        params.put("shipment_id", passData.getShippingID());
        if (passData.getAttachmentList() == null || passData.getAttachmentList().isEmpty()) {
            params.put("has_resi_photo", "0");
        } else {
            params.put("has_resi_photo", "1");
            params.put("resi_photo_all", getPhotos(passData.getAttachmentList()));
            params.put("resi_pic_obj", getPhotosObj(passData.getAttachmentList()));
        }
        return params;
    }

    public static TKPDMapParam<String, String> paramEditShippingSubmit(ShippingParamsPostModel passData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("resolution_id", passData.getResolutionID());
        params.put("conversation_id", passData.getConversationID());
        params.put("post_key", passData.getPostKey());
        params.put("file_uploaded", getFileUploaded(passData));
        return params;
    }
}
