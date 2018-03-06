package com.tokopedia.inbox.inboxchat.uploadimage;


import java.util.ArrayList;

/**
 * @author by nisie on 8/31/17.
 */

public class UploadImageChatRequestModel {
    private boolean isValidateSuccess;

    String uploadHost;
    String serverId;

    private String attachmentString;
    private ArrayList<ImageUpload> listUpload;
    private int isSubmitSuccess;

    public UploadImageChatRequestModel() {
        this.isValidateSuccess = false;
        this.uploadHost = "";
        this.serverId = "";
        this.isSubmitSuccess= 0;
    }


    public void setValidateSuccess(boolean validateSuccess) {
        isValidateSuccess = validateSuccess;
    }

    public boolean isValidateSuccess() {
        return isValidateSuccess;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getAttachmentString() {
        return attachmentString;
    }

    public void setAttachmentString(String attachmentString) {
        this.attachmentString = attachmentString;
    }

    public void setListUpload(ArrayList<ImageUpload> listUpload) {
        this.listUpload = listUpload;
    }

    public ArrayList<ImageUpload> getListUpload() {
        return listUpload;
    }

    public void setIsSubmitSuccess(int isSubmitSuccess) {
        this.isSubmitSuccess = isSubmitSuccess;
    }

    public int getIsSubmitSuccess() {
        return isSubmitSuccess;
    }


}
