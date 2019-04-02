package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class AttachmentDataDomain implements Parcelable {

    private String fullUrl;
    private String thumbnail;
    private int isVideo;


    public AttachmentDataDomain(String fullUrl, String thumbnail, int isVideo) {
        this.fullUrl = fullUrl;
        this.thumbnail = thumbnail;
        this.isVideo = isVideo;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
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
        dest.writeInt(this.isVideo);
    }

    protected AttachmentDataDomain(Parcel in) {
        this.fullUrl = in.readString();
        this.thumbnail = in.readString();
        this.isVideo = in.readInt();
    }

    public static final Creator<AttachmentDataDomain> CREATOR = new Creator<AttachmentDataDomain>() {
        @Override
        public AttachmentDataDomain createFromParcel(Parcel source) {
            return new AttachmentDataDomain(source);
        }

        @Override
        public AttachmentDataDomain[] newArray(int size) {
            return new AttachmentDataDomain[size];
        }
    };
}
