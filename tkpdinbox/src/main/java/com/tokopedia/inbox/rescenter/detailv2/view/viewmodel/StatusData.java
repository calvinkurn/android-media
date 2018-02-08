package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/10/17.
 */

public class StatusData implements Parcelable {
    public static final Parcelable.Creator<StatusData> CREATOR = new Parcelable.Creator<StatusData>() {
        @Override
        public StatusData createFromParcel(Parcel source) {
            return new StatusData(source);
        }

        @Override
        public StatusData[] newArray(int size) {
            return new StatusData[size];
        }
    };
    private String statusText;

    public StatusData() {
    }

    protected StatusData(Parcel in) {
        this.statusText = in.readString();
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.statusText);
    }
}
