package com.tokopedia.inbox.rescenter.shipping.interactor;

import com.tokopedia.core.database.model.AttachmentResCenterDB;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsPostModel;

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

    public static TKPDMapParam<String, String> paramInputShippingValidation(InputShippingParamsPostModel passData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("resolution_id", passData.getResolutionID());
        params.put("shipping_ref", passData.getShippingNumber());
        params.put("shipment_id", passData.getShippingID());
        params.put("has_resi_photo", String.valueOf(passData.getAttachmentList() == null || passData.getAttachmentList().isEmpty() ? 0 : 1));
        params.put("resi_photo_all", getPhotos(passData.getAttachmentList()));
        params.put("resi_pic_obj", getPhotosObj(passData.getAttachmentList()));
        return params;
    }

    private static String getPhotos(List<AttachmentResCenterDB> attachmentList) {
        if (attachmentList == null || attachmentList.isEmpty()) {
            return "";
        }

        String allPhoto = "";
        for (int i = 0; i < attachmentList.size(); i++) {
            if (i == attachmentList.size() - 1) {
                allPhoto += attachmentList.get(i).imageUUID;
            } else {
                allPhoto += attachmentList.get(i).imageUUID + "~";
            }
        }

        return allPhoto;
    }

    private static String getPhotosObj(List<AttachmentResCenterDB> attachmentList) {

        JSONObject reviewPhotos = new JSONObject();
        try {
            for (AttachmentResCenterDB image : attachmentList) {
                reviewPhotos.put(image.imageUUID, generateObject(image));
            }
            return reviewPhotos.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    private static JSONObject generateObject(AttachmentResCenterDB image) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image_id", image.imageUUID);
        jsonObject.put("file_x", "80");
        jsonObject.put("file_y", "80");
        jsonObject.put("is_temp", "1");
        return jsonObject;
    }

    public static TKPDMapParam<String, String> paramInputShippingSubmit(InputShippingParamsPostModel passData) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put("resolution_id", passData.getResolutionID());
        params.put("post_key", passData.getPostKey());
        params.put("file_uploaded", getFileUploaded(passData));
        return params;
    }

    private static String getFileUploaded(InputShippingParamsPostModel passData) {
        try {
            JSONObject jsonObject = new JSONObject();
            for (AttachmentResCenterDB attachment: passData.getAttachmentList()) {
                jsonObject.put(attachment.imageUUID, attachment.picObj);
            }
            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
