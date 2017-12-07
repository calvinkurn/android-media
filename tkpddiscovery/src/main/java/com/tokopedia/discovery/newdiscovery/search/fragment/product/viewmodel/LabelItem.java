package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class LabelItem implements Parcelable {
    private String color;
    private String title;

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
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
        dest.writeString(this.color);
        dest.writeString(this.title);
    }

    public LabelItem() {
    }

    protected LabelItem(Parcel in) {
        this.color = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<LabelItem> CREATOR = new Parcelable.Creator<LabelItem>() {
        @Override
        public LabelItem createFromParcel(Parcel source) {
            return new LabelItem(source);
        }

        @Override
        public LabelItem[] newArray(int size) {
            return new LabelItem[size];
        }
    };
}
