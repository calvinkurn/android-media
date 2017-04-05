package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/13/17.
 */

public class AddressReturData implements Parcelable {
    private String addressReturDate;
    private String addressText;

    public String getAddressReturDate() {
        return addressReturDate;
    }

    public void setAddressReturDate(String addressReturDate) {
        this.addressReturDate = addressReturDate;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addressReturDate);
        dest.writeString(this.addressText);
    }

    public AddressReturData() {
    }

    protected AddressReturData(Parcel in) {
        this.addressReturDate = in.readString();
        this.addressText = in.readString();
    }

    public static final Parcelable.Creator<AddressReturData> CREATOR = new Parcelable.Creator<AddressReturData>() {
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
