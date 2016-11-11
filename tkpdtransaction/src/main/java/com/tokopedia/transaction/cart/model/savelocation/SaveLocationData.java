package com.tokopedia.transaction.cart.model.savelocation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvarisi on 11/3/2016.
 */

public class SaveLocationData implements Parcelable {
    private String message;
    private String status;

    public SaveLocationData() {
    }

    protected SaveLocationData(Parcel in) {
        message = in.readString();
        status = in.readString();
    }

    public static final Creator<SaveLocationData> CREATOR = new Creator<SaveLocationData>() {
        @Override
        public SaveLocationData createFromParcel(Parcel in) {
            return new SaveLocationData(in);
        }

        @Override
        public SaveLocationData[] newArray(int size) {
            return new SaveLocationData[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(status);
    }
}
