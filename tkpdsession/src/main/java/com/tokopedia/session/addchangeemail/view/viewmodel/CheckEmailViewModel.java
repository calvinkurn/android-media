package com.tokopedia.session.addchangeemail.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailViewModel implements Parcelable {
    private boolean isExist;

    public CheckEmailViewModel(boolean isExist) {
        this.isExist = isExist;
    }

    public boolean isExist() {

        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isExist ? (byte) 1 : (byte) 0);
    }

    public CheckEmailViewModel() {
    }

    protected CheckEmailViewModel(Parcel in) {
        this.isExist = in.readByte() != 0;
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
