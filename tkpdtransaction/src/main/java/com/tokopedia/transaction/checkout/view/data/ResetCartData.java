package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public class ResetCartData implements Parcelable {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
    }

    public ResetCartData() {
    }

    protected ResetCartData(Parcel in) {
        this.success = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ResetCartData> CREATOR = new Parcelable.Creator<ResetCartData>() {
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
