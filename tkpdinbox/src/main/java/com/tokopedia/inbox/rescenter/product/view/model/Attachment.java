package com.tokopedia.inbox.rescenter.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 3/28/17.
 */

public class Attachment implements Parcelable {

    private String thumbUrl;
    private String url;

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.thumbUrl);
        dest.writeString(this.url);
    }

    public Attachment() {
    }

    protected Attachment(Parcel in) {
        this.thumbUrl = in.readString();
        this.url = in.readString();
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
