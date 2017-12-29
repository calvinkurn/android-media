package com.tokopedia.seller.shop.open.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class ResponseIsReserveDomain implements Parcelable {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("data")
    @Expose
    private Data data;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.header, flags);
        dest.writeParcelable(this.data, flags);
    }

    public ResponseIsReserveDomain() {
    }

    protected ResponseIsReserveDomain(Parcel in) {
        this.header = in.readParcelable(Header.class.getClassLoader());
        this.data = in.readParcelable(Data.class.getClassLoader());
    }

    public static final Creator<ResponseIsReserveDomain> CREATOR = new Creator<ResponseIsReserveDomain>() {
        @Override
        public ResponseIsReserveDomain createFromParcel(Parcel source) {
            return new ResponseIsReserveDomain(source);
        }

        @Override
        public ResponseIsReserveDomain[] newArray(int size) {
            return new ResponseIsReserveDomain[size];
        }
    };
}
