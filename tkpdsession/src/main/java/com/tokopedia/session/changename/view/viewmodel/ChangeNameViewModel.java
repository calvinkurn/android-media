package com.tokopedia.session.changename.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNameViewModel implements Parcelable {
    private boolean isSuccess;

    public ChangeNameViewModel(boolean isSuccess) {
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

    protected ChangeNameViewModel(Parcel in) {
        this.isSuccess = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ChangeNameViewModel> CREATOR = new Parcelable.Creator<ChangeNameViewModel>() {
        @Override
        public ChangeNameViewModel createFromParcel(Parcel source) {
            return new ChangeNameViewModel(source);
        }

        @Override
        public ChangeNameViewModel[] newArray(int size) {
            return new ChangeNameViewModel[size];
        }
    };
}
