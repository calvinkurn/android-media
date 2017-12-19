package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/17/17.
 */

public class AwbAttachmentViewModel implements Parcelable {
    public static final Parcelable.Creator<AwbAttachmentViewModel> CREATOR = new Parcelable.Creator<AwbAttachmentViewModel>() {
        @Override
        public AwbAttachmentViewModel createFromParcel(Parcel source) {
            return new AwbAttachmentViewModel(source);
        }

        @Override
        public AwbAttachmentViewModel[] newArray(int size) {
            return new AwbAttachmentViewModel[size];
        }
    };
    private String imageUrl;
    private String imageThumbUrl;

    public AwbAttachmentViewModel() {
    }

    protected AwbAttachmentViewModel(Parcel in) {
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
