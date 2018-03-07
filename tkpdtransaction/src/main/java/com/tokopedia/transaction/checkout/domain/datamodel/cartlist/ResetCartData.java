package com.tokopedia.transaction.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public class ResetCartData implements Parcelable {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public ResetCartData() {
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
    }

    protected ResetCartData(Parcel in) {
        this.success = in.readByte() != 0;
        this.message = in.readString();
    }

    public static final Creator<ResetCartData> CREATOR = new Creator<ResetCartData>() {
        @Override
        public ResetCartData createFromParcel(Parcel source) {
            return new ResetCartData(source);
        }

        @Override
        public ResetCartData[] newArray(int size) {
            return new ResetCartData[size];
        }
    };
}
