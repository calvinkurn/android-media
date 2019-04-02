package com.tokopedia.session.addchangeemail.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailViewModel implements Parcelable {
    private boolean isExist;
    private String message;

    public CheckEmailViewModel(boolean isExist, String message) {
        this.isExist = isExist;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isExist() {

        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public CheckEmailViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isExist ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
    }

    protected CheckEmailViewModel(Parcel in) {
        this.isExist = in.readByte() != 0;
        this.message = in.readString();
    }

    public static final Creator<CheckEmailViewModel> CREATOR = new Creator<CheckEmailViewModel>() {
        @Override
        public CheckEmailViewModel createFromParcel(Parcel source) {
            return new CheckEmailViewModel(source);
        }

        @Override
        public CheckEmailViewModel[] newArray(int size) {
            return new CheckEmailViewModel[size];
        }
    };
}
