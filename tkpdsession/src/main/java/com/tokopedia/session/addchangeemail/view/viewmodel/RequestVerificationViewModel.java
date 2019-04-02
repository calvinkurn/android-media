package com.tokopedia.session.addchangeemail.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 09/03/18.
 */

public class RequestVerificationViewModel implements Parcelable {
    private boolean isSuccess;

    public RequestVerificationViewModel(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {

        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSuccess ? (byte) 1 : (byte) 0);
    }

    protected RequestVerificationViewModel(Parcel in) {
        this.isSuccess = in.readByte() != 0;
    }

    public static final Parcelable.Creator<RequestVerificationViewModel> CREATOR = new Parcelable.Creator<RequestVerificationViewModel>() {
        @Override
        public RequestVerificationViewModel createFromParcel(Parcel source) {
            return new RequestVerificationViewModel(source);
        }

        @Override
        public RequestVerificationViewModel[] newArray(int size) {
            return new RequestVerificationViewModel[size];
        }
    };
}
