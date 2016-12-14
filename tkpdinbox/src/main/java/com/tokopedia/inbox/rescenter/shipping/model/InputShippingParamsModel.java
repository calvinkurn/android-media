package com.tokopedia.inbox.rescenter.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 12/14/16.
 */

public class InputShippingParamsModel implements Parcelable {

    private String resolutionID;

    private String conversationID;

    private String shippingID;

    private String shippingRefNum;

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

    public String getShippingID() {
        return shippingID;
    }

    public void setShippingID(String shippingID) {
        this.shippingID = shippingID;
    }

    public String getShippingRefNum() {
        return shippingRefNum;
    }

    public void setShippingRefNum(String shippingRefNum) {
        this.shippingRefNum = shippingRefNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resolutionID);
        dest.writeString(this.conversationID);
        dest.writeString(this.shippingID);
        dest.writeString(this.shippingRefNum);
    }

    public InputShippingParamsModel() {
    }

    protected InputShippingParamsModel(Parcel in) {
        this.resolutionID = in.readString();
        this.conversationID = in.readString();
        this.shippingID = in.readString();
        this.shippingRefNum = in.readString();
    }

    public static final Parcelable.Creator<InputShippingParamsModel> CREATOR = new Parcelable.Creator<InputShippingParamsModel>() {
        @Override
        public InputShippingParamsModel createFromParcel(Parcel source) {
            return new InputShippingParamsModel(source);
        }

        @Override
        public InputShippingParamsModel[] newArray(int size) {
            return new InputShippingParamsModel[size];
        }
    };
}
