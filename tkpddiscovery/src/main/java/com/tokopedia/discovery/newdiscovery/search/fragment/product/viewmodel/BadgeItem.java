package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class BadgeItem implements Parcelable {
    private String imageUrl;
    private String title;
    private boolean isShown;

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

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public BadgeItem() {
    }

    public BadgeItem(String imageUrl, String title, boolean isShown) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.isShown = isShown;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeByte(this.isShown ? (byte) 1 : (byte) 0);
    }

    protected BadgeItem(Parcel in) {
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.isShown = in.readByte() != 0;
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
