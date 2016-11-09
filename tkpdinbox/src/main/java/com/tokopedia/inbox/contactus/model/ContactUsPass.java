package com.tokopedia.inbox.contactus.model;

import com.tokopedia.core.inboxreputation.model.ImageUpload;
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


    private static final String PARAM_TICKET_CATEGORY_ID = "ticket_category_id";

    private static final String PARAM_MESSAGE_CATEGORY = "message_category";
    private static final String PARAM_MESSAGE_BODY = "message_body";
    private static final String PARAM_INVOICE_NUMBER = "invoice_number";
    private static final String PARAM_SERVER_ID = "server_id";

    private static final String PARAM_POST_KEY = "post_key";
    private static final String PARAM_FILE_UPLOADED = "file_uploaded";
    private static final String PARAM_P_PHOTO = "p_photo";
    private static final String PARAM_PHOTO_ALL = "p_photo_all";
    private static final String PARAM_PHOTO_OBJ = "prd_pic_obj";
    private static final String PARAM_IMAGE_ID = "image_id";
    private static final String PARAM_ATTACHMENT_STRING = "attachment_string";
    private static final String PARAM_IS_TEMP = "is_temp";

    String ticketCategoryId;
    String messageBody;
    String invoiceNumber;
    String serverId;
    ArrayList<ImageUpload> attachment;

    String postKey;
    String fileUploaded;
    private GeneratedHost generatedHost;

    public String getTicketCategoryId() {
        return ticketCategoryId;
    }

    public void setTicketCategoryId(String ticketCategoryId) {
        this.ticketCategoryId = ticketCategoryId;
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

    public HashMap<String, String> getContactUsFormParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_TICKET_CATEGORY_ID, getTicketCategoryId());
        return param;
    }

    public HashMap<String, String> getCreateTicketValidationParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put(PARAM_MESSAGE_CATEGORY, getTicketCategoryId());
        param.put(PARAM_MESSAGE_BODY, getMessageBody());
        if (getAttachment() != null && getAttachment().size() > 0) {
            param.put(PARAM_ATTACHMENT_STRING, getPhotoAll());
            param.put(PARAM_PHOTO_ALL, getPhotoAll());
            param.put(PARAM_P_PHOTO, "1");
            param.put(PARAM_PHOTO_OBJ, getPhotoObj());

        }
        if (getInvoiceNumber() != null)
            param.put(PARAM_INVOICE_NUMBER, getInvoiceNumber());
        if (getServerId() != null)
            param.put(PARAM_SERVER_ID, getServerId());
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
        return param;
    }

    public GeneratedHost getGeneratedHost() {
        return generatedHost;
    }

    public void setGeneratedHost(GeneratedHost generatedHost) {
        this.generatedHost = generatedHost;
    }
}
