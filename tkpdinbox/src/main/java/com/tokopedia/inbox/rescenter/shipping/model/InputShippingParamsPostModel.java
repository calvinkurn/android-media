package com.tokopedia.inbox.rescenter.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.database.model.AttachmentResCenterDB;

import java.util.List;

/**
 * Created by hangnadi on 12/19/16.
 */

public class InputShippingParamsPostModel implements Parcelable {

    private String resolutionID;
    private String conversationID;
    private String shippingNumber;
    private String shippingID;
    private List<AttachmentResCenterDB> attachmentList;
    private String serverID;
    private String uploadHost;
    private String postKey;
    private String token;
    private boolean statusInputShipping;

    public String getResolutionID() {
        return resolutionID;
    }

    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getShippingNumber() {
        return shippingNumber;
    }

    public void setShippingNumber(String shippingNumber) {
        this.shippingNumber = shippingNumber;
    }

    public String getShippingID() {
        return shippingID;
    }

    public void setShippingID(String shippingID) {
        this.shippingID = shippingID;
    }

    public List<AttachmentResCenterDB> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentResCenterDB> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getUploadHost() {
        return uploadHost;
    }

    public void setUploadHost(String uploadHost) {
        this.uploadHost = uploadHost;
    }

    public InputShippingParamsPostModel() {
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isStatusInputShipping() {
        return statusInputShipping;
    }

    public void setStatusInputShipping(boolean statusInputShipping) {
        this.statusInputShipping = statusInputShipping;
    }

    public static class Builder {
        private String resolutionID;
        private String conversationID;
        private String shippingNumber;
        private String shippingID;
        private List<AttachmentResCenterDB> attachmentList;

        public Builder() {
        }

        public Builder setResolutionID(String resolutionID) {
            this.resolutionID = resolutionID;
            return this;
        }

        public Builder setConversationID(String conversationID) {
            this.conversationID = conversationID;
            return this;
        }

        public Builder setShippingNumber(String shippingNumber) {
            this.shippingNumber = shippingNumber;
            return this;
        }

        public Builder setShippingID(String shippingID) {
            this.shippingID = shippingID;
            return this;
        }

        public Builder setAttachmentList(List<AttachmentResCenterDB> attachmentList) {
            this.attachmentList = attachmentList;
            return this;
        }

        public InputShippingParamsPostModel build() {
            InputShippingParamsPostModel postModel = new InputShippingParamsPostModel();
            postModel.setShippingID(shippingID);
            postModel.setConversationID(conversationID);
            postModel.setResolutionID(resolutionID);
            postModel.setShippingNumber(shippingNumber);
            postModel.setAttachmentList(attachmentList);
            return postModel;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resolutionID);
        dest.writeString(this.conversationID);
        dest.writeString(this.shippingNumber);
        dest.writeString(this.shippingID);
        dest.writeTypedList(this.attachmentList);
        dest.writeString(this.serverID);
        dest.writeString(this.uploadHost);
        dest.writeString(this.postKey);
        dest.writeString(this.token);
    }

    protected InputShippingParamsPostModel(Parcel in) {
        this.resolutionID = in.readString();
        this.conversationID = in.readString();
        this.shippingNumber = in.readString();
        this.shippingID = in.readString();
        this.attachmentList = in.createTypedArrayList(AttachmentResCenterDB.CREATOR);
        this.serverID = in.readString();
        this.uploadHost = in.readString();
        this.postKey = in.readString();
        this.token = in.readString();
    }

    public static final Creator<InputShippingParamsPostModel> CREATOR = new Creator<InputShippingParamsPostModel>() {
        @Override
        public InputShippingParamsPostModel createFromParcel(Parcel source) {
            return new InputShippingParamsPostModel(source);
        }

        @Override
        public InputShippingParamsPostModel[] newArray(int size) {
            return new InputShippingParamsPostModel[size];
        }
    };
}
