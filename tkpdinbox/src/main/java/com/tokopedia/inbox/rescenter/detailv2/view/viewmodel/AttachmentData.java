package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 10/11/17.
 */

public class AttachmentData implements Parcelable {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;

    private String imageUrl;
    private String imageThumbUrl;
    private int isVideo;

    public AttachmentData() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageThumbUrl() {
        return imageThumbUrl;
    }

    public void setImageThumbUrl(String imageThumbUrl) {
        this.imageThumbUrl = imageThumbUrl;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageThumbUrl);
        dest.writeInt(this.isVideo);
    }

    protected AttachmentData(Parcel in) {
        this.imageUrl = in.readString();
        this.imageThumbUrl = in.readString();
        this.isVideo = in.readInt();
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
