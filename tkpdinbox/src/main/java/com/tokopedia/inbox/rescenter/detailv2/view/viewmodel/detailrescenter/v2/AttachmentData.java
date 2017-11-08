package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class AttachmentData implements Parcelable {

    private String fullUrl;
    private String thumbnail;

    public AttachmentData(String fullUrl, String thumbnail) {
        this.fullUrl = fullUrl;
        this.thumbnail = thumbnail;
    }


    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullUrl);
        dest.writeString(this.thumbnail);
    }

    protected AttachmentData(Parcel in) {
        this.fullUrl = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Creator<AttachmentData> CREATOR = new Creator<AttachmentData>() {
        @Override
        public AttachmentData createFromParcel(Parcel source) {
            return new AttachmentData(source);
        }

        @Override
        public AttachmentData[] newArray(int size) {
            return new AttachmentData[size];
        }
    };
}
