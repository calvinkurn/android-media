package com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoasfs on 29/08/17.
 */

public class FreeReturnViewModel implements Parcelable {
    private String info;
    private String link;

    public FreeReturnViewModel(String info, String link) {
        this.info = info;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.info);
        dest.writeString(this.link);
    }

    protected FreeReturnViewModel(Parcel in) {
        this.info = in.readString();
        this.link = in.readString();
    }

    public static final Creator<FreeReturnViewModel> CREATOR = new Creator<FreeReturnViewModel>() {
        @Override
        public FreeReturnViewModel createFromParcel(Parcel source) {
            return new FreeReturnViewModel(source);
        }

        @Override
        public FreeReturnViewModel[] newArray(int size) {
            return new FreeReturnViewModel[size];
        }
    };
}
