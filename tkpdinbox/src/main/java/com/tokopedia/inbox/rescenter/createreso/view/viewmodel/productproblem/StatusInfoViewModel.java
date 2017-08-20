package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class StatusInfoViewModel implements Parcelable {
    private boolean show;
    private String date;

    public StatusInfoViewModel(boolean show, String date) {
        this.show = show;
        this.date = date;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.show ? (byte) 1 : (byte) 0);
        dest.writeString(this.date);
    }

    protected StatusInfoViewModel(Parcel in) {
        this.show = in.readByte() != 0;
        this.date = in.readString();
    }

    public static final Creator<StatusInfoViewModel> CREATOR = new Creator<StatusInfoViewModel>() {
        @Override
        public StatusInfoViewModel createFromParcel(Parcel source) {
            return new StatusInfoViewModel(source);
        }

        @Override
        public StatusInfoViewModel[] newArray(int size) {
            return new StatusInfoViewModel[size];
        }
    };
}
