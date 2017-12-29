package com.tokopedia.seller.shop.setting.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Header implements Parcelable {

    @SerializedName("process_time")
    @Expose
    private double processTime;
    @SerializedName("messages")
    @Expose
    private Object messages;
    @SerializedName("reason")
    @Expose
    private String reason;

    public double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }

    public Object getMessages() {
        return messages;
    }

    public void setMessages(Object messages) {
        this.messages = messages;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.processTime);
        dest.writeParcelable(this.messages, flags);
        dest.writeString(this.reason);
    }

    public Header() {
    }

    protected Header(Parcel in) {
        this.processTime = in.readDouble();
        this.messages = in.readParcelable(Object.class.getClassLoader());
        this.reason = in.readString();
    }

    public static final Parcelable.Creator<Header> CREATOR = new Parcelable.Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel source) {
            return new Header(source);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };
}