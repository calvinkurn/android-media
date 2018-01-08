package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 14/12/17.
 */

public class FreeReturnData implements Parcelable {
    private boolean isFreeReturnShow;
    private String freeReturnText;
    private String freeReturnLink;

    public FreeReturnData(boolean isFreeReturnShow, String freeReturnText, String freeReturnLink) {
        this.isFreeReturnShow = isFreeReturnShow;
        this.freeReturnText = freeReturnText;
        this.freeReturnLink = freeReturnLink;
    }

    public String getFreeReturnLink() {
        return freeReturnLink;
    }

    public void setFreeReturnLink(String freeReturnLink) {
        this.freeReturnLink = freeReturnLink;
    }

    public boolean isFreeReturnShow() {
        return isFreeReturnShow;
    }

    public void setFreeReturnShow(boolean freeReturnShow) {
        isFreeReturnShow = freeReturnShow;
    }

    public String getFreeReturnText() {
        return freeReturnText;
    }

    public void setFreeReturnText(String freeReturnText) {
        this.freeReturnText = freeReturnText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isFreeReturnShow ? (byte) 1 : (byte) 0);
        dest.writeString(this.freeReturnText);
        dest.writeString(this.freeReturnLink);
    }

    protected FreeReturnData(Parcel in) {
        this.isFreeReturnShow = in.readByte() != 0;
        this.freeReturnText = in.readString();
        this.freeReturnLink = in.readString();
    }

    public static final Creator<FreeReturnData> CREATOR = new Creator<FreeReturnData>() {
        @Override
        public FreeReturnData createFromParcel(Parcel source) {
            return new FreeReturnData(source);
        }

        @Override
        public FreeReturnData[] newArray(int size) {
            return new FreeReturnData[size];
        }
    };
}
