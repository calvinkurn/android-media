package com.tokopedia.session.addchangeemail.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailViewModel implements Parcelable {
    private boolean isSuccess;

    public AddEmailViewModel(boolean isSuccess) {
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

    public AddEmailViewModel() {
    }

    protected AddEmailViewModel(Parcel in) {
        this.isSuccess = in.readByte() != 0;
    }

    public static final Parcelable.Creator<AddEmailViewModel> CREATOR = new Parcelable.Creator<AddEmailViewModel>() {
        @Override
        public AddEmailViewModel createFromParcel(Parcel source) {
            return new AddEmailViewModel(source);
        }

        @Override
        public AddEmailViewModel[] newArray(int size) {
            return new AddEmailViewModel[size];
        }
    };
}
