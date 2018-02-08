package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class BadgeItem implements Parcelable {
    private String imageUrl;
    private String title;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
    }

    public BadgeItem() {
    }

    protected BadgeItem(Parcel in) {
        this.imageUrl = in.readString();
        this.title = in.readString();
    }

    public static final Creator<BadgeItem> CREATOR = new Creator<BadgeItem>() {
        @Override
        public BadgeItem createFromParcel(Parcel source) {
            return new BadgeItem(source);
        }

        @Override
        public BadgeItem[] newArray(int size) {
            return new BadgeItem[size];
        }
    };
}
