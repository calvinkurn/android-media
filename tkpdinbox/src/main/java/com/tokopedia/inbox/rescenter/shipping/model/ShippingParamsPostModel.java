package com.tokopedia.inbox.rescenter.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.database.model.AttachmentResCenterVersion2DB;

import java.util.List;

/**
 * Created by hangnadi on 12/19/16.
 */

public class ShippingParamsPostModel implements Parcelable {

    private String resolutionID;
    private String conversationID;
    private String shippingNumber;
    private String shippingID;
    private List<AttachmentResCenterVersion2DB> attachmentList;
    private String serverID;
    private String uploadHost;
    private String postKey;
    private String token;
    private Integer userId;
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

    public List<AttachmentResCenterVersion2DB> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentResCenterVersion2DB> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public ShippingParamsPostModel() {
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
        private List<AttachmentResCenterVersion2DB> attachmentList;

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

        public Builder setAttachmentList(List<AttachmentResCenterVersion2DB> attachmentList) {
            this.attachmentList = attachmentList;
            return this;
        }

        public ShippingParamsPostModel build() {
            ShippingParamsPostModel postModel = new ShippingParamsPostModel();
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

    protected ShippingParamsPostModel(Parcel in) {
        this.resolutionID = in.readString();
        this.conversationID = in.readString();
        this.shippingNumber = in.readString();
        this.shippingID = in.readString();
        this.attachmentList = in.createTypedArrayList(AttachmentResCenterVersion2DB.CREATOR);
        this.serverID = in.readString();
        this.uploadHost = in.readString();
        this.postKey = in.readString();
        this.token = in.readString();
    }

    public static final Creator<ShippingParamsPostModel> CREATOR = new Creator<ShippingParamsPostModel>() {
        @Override
        public ShippingParamsPostModel createFromParcel(Parcel source) {
            return new ShippingParamsPostModel(source);
        }

        @Override
        public ShippingParamsPostModel[] newArray(int size) {
            return new ShippingParamsPostModel[size];
        }
    };
}
