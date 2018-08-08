package com.tokopedia.inbox.rescenter.historyawb.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/24/17.
 */

public class Attachment implements Parcelable {
    private String url;
    private String thumbnailUrl;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.thumbnailUrl);
    }

    public Attachment() {
    }

    protected Attachment(Parcel in) {
        this.url = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel source) {
            return new Attachment(source);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
}
