package com.tokopedia.session.addchangepassword.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordViewModel implements Parcelable {
    private boolean isSuccess;

    public AddPasswordViewModel(boolean isSuccess) {
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

    protected AddPasswordViewModel(Parcel in) {
        this.isSuccess = in.readByte() != 0;
    }

    public static final Parcelable.Creator<AddPasswordViewModel> CREATOR = new Parcelable.Creator<AddPasswordViewModel>() {
        @Override
        public AddPasswordViewModel createFromParcel(Parcel source) {
            return new AddPasswordViewModel(source);
        }

        @Override
        public AddPasswordViewModel[] newArray(int size) {
            return new AddPasswordViewModel[size];
        }
    };
}
