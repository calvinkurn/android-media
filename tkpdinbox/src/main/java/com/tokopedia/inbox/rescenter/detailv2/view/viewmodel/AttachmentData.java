package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 10/11/17.
 */

public class AttachmentData implements Parcelable {

    public static final Parcelable.Creator<AttachmentData> CREATOR = new Parcelable.Creator<AttachmentData>() {
        @Override
        public AttachmentData createFromParcel(Parcel source) {
            return new AttachmentData(source);
        }

        @Override
        public AttachmentData[] newArray(int size) {
            return new AttachmentData[size];
        }
    };
    private String imageUrl;
    private String imageThumbUrl;

    public AttachmentData() {
    }

    protected AttachmentData(Parcel in) {
        this.imageUrl = in.readString();
        this.imageThumbUrl = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageThumbUrl);
    }
}
