package com.tokopedia.transaction.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 02/03/18.
 */

public class CartTickerErrorData implements Parcelable {

    private String errorInfo;
    private String actionInfo;

    private CartTickerErrorData(Builder builder) {
        errorInfo = builder.errorInfo;
        actionInfo = builder.actionInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public String getActionInfo() {
        return actionInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.errorInfo);
        dest.writeString(this.actionInfo);
    }

    public CartTickerErrorData() {
    }

    protected CartTickerErrorData(Parcel in) {
        this.errorInfo = in.readString();
        this.actionInfo = in.readString();
    }

    public static final Creator<CartTickerErrorData> CREATOR = new Creator<CartTickerErrorData>() {
        @Override
        public CartTickerErrorData createFromParcel(Parcel source) {
            return new CartTickerErrorData(source);
        }

        @Override
        public CartTickerErrorData[] newArray(int size) {
            return new CartTickerErrorData[size];
        }
    };

    public static final class Builder {
        private String errorInfo;
        private String actionInfo;

        public Builder() {
        }

        public Builder errorInfo(String val) {
            errorInfo = val;
            return this;
        }

        public Builder actionInfo(String val) {
            actionInfo = val;
            return this;
        }

        public CartTickerErrorData build() {
            return new CartTickerErrorData(this);
        }
    }
}
