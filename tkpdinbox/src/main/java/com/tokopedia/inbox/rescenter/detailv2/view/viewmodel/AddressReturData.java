package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/13/17.
 */

public class AddressReturData implements Parcelable {
    private String addressReturDate;
    private String addressReturDateTimestamp;
    private String addressText;
    private String addressID;
    private String conversationID;

    public AddressReturData() {
    }

    public String getAddressReturDate() {
        return addressReturDate;
    }

    public void setAddressReturDate(String addressReturDate) {
        this.addressReturDate = addressReturDate;
    }

    public String getAddressReturDateTimestamp() {
        return addressReturDateTimestamp;
    }

    public void setAddressReturDateTimestamp(String addressReturDateTimestamp) {
        this.addressReturDateTimestamp = addressReturDateTimestamp;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addressReturDate);
        dest.writeString(this.addressReturDateTimestamp);
        dest.writeString(this.addressText);
        dest.writeString(this.addressID);
        dest.writeString(this.conversationID);
    }

    protected AddressReturData(Parcel in) {
        this.addressReturDate = in.readString();
        this.addressReturDateTimestamp = in.readString();
        this.addressText = in.readString();
        this.addressID = in.readString();
        this.conversationID = in.readString();
    }

    public static final Creator<AddressReturData> CREATOR = new Creator<AddressReturData>() {
        @Override
        public AddressReturData createFromParcel(Parcel source) {
            return new AddressReturData(source);
        }

        @Override
        public AddressReturData[] newArray(int size) {
            return new AddressReturData[size];
        }
    };
}
