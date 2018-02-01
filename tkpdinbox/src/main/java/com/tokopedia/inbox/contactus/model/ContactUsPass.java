package com.tokopedia.inbox.contactus.model;

import com.tokopedia.core.network.retrofit.response.GeneratedHost;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsPass {


    private static final String PARAM_SOLUTION_ID = "solution_id";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_MESSAGE = "message";
    private static final String PARAM_ORDER_ID = "order_id";
    private static final String PARAM_INVOICE = "invoice";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_TAG = "tag";


    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";
    private static final String PARAM_P_PHOTO = "p_photo";
    private static final String PARAM_PHOTO_ALL = "p_photo_all";
    private static final String PARAM_PHOTO_OBJ = "prd_pic_obj";
    private static final String PARAM_IMAGE_ID = "image_id";
    private static final String PARAM_ATTACHMENT_STRING = "attachment_string";
    private static final String PARAM_IS_TEMP = "is_temp";
    private static final String PARAM_UTM_SOURCE = "utm_source";
    private static final String PARAM_FLAG_APP = "flag_app";

    String solutionId;
    String messageBody;
    String invoiceNumber;
    String serverId;
    ArrayList<ImageUpload> attachment;

    String postKey;
    private GeneratedHost generatedHost;
    private String email;
    private String name;
    private String phoneNumber;
    private String orderId;
    private String tag;


    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public ArrayList<ImageUpload> getAttachment() {
        return attachment;
    }

    public void setAttachment(ArrayList<ImageUpload> attachment) {
        this.attachment = attachment;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    private String getFileUploaded() {
        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : attachment) {
                reviewPhotos.put(image.getImageId(), image.getPicObj());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }

    public HashMap<String, String> getCreateTicketValidationParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_SOLUTION_ID, getSolutionId());
        param.put(PARAM_MESSAGE, getMessageBody());
        param.put(PARAM_EMAIL, getEmail());
        param.put(PARAM_NAME, getName());
        param.put(PARAM_ORDER_ID, getOrderId());
        if (getInvoiceNumber() != null && getInvoiceNumber().length() > 0) {
            param.put(PARAM_INVOICE, getInvoiceNumber());
        }
        param.put(PARAM_PHONE, getPhoneNumber());
        param.put(PARAM_TAG, getPhoneNumber());
        param.put(PARAM_UTM_SOURCE, "android");
        param.put(PARAM_FLAG_APP,"1");

        if (getAttachment() != null && getAttachment().size() > 0) {
            param.put(PARAM_ATTACHMENT_STRING, getPhotoAll());
            param.put(PARAM_PHOTO_ALL, getPhotoAll());
            param.put(PARAM_P_PHOTO, "1");
            param.put(PARAM_PHOTO_OBJ, getPhotoObj());

        }
        return param;
    }

    private String getPhotoObj() {
        JSONObject reviewPhotos = new JSONObject();
        try {
            for (ImageUpload image : getAttachment()) {
                JSONObject photoObj = new JSONObject();
                photoObj.put(PARAM_IMAGE_ID, image.getImageId());
                photoObj.put(PARAM_IS_TEMP, "1");
                reviewPhotos.put(image.getImageId(), photoObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviewPhotos.toString();
    }

    private String getPhotoAll() {
        String attachmentString = "";
        for (ImageUpload imageUpload : attachment) {
            if (imageUpload != null) {
                attachmentString += "~" + imageUpload.getImageId();
            }
        }
        attachmentString = attachmentString.replace("~~", "~");
        if (!attachmentString.isEmpty())
            attachmentString = attachmentString.substring(1);
        return attachmentString;
    }

    public Map<String, String> getSubmitParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_POST_KEY, getPostKey());
        param.put(PARAM_FILE_UPLOADED, getFileUploaded());
        param.put(PARAM_UTM_SOURCE, "android");
        param.put(PARAM_FLAG_APP,"1");
        return param;
    }

    public GeneratedHost getGeneratedHost() {
        return generatedHost;
    }

    public void setGeneratedHost(GeneratedHost generatedHost) {
        this.generatedHost = generatedHost;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}