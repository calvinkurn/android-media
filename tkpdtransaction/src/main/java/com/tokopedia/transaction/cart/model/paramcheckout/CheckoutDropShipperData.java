package com.tokopedia.transaction.cart.model.paramcheckout;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 11/28/16.
 */

public class CheckoutDropShipperData implements Parcelable {
    private String key;
    private String value;

    private CheckoutDropShipperData(Builder builder) {
        setKey(builder.key);
        setValue(builder.value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
    }

    public CheckoutDropShipperData() {
    }

    protected CheckoutDropShipperData(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public static final Creator<CheckoutDropShipperData> CREATOR
            = new Creator<CheckoutDropShipperData>() {
        @Override
        public CheckoutDropShipperData createFromParcel(Parcel source) {
            return new CheckoutDropShipperData(source);
        }

        @Override
        public CheckoutDropShipperData[] newArray(int size) {
            return new CheckoutDropShipperData[size];
        }
    };


    public static final class Builder {
        private String key;
        private String value;

        public Builder() {
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public Builder value(String val) {
            value = val;
            return this;
        }

        public CheckoutDropShipperData build() {
            return new CheckoutDropShipperData(this);
        }
    }
}
