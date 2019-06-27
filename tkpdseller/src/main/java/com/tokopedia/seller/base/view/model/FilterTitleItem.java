package com.tokopedia.seller.base.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nathaniel on 1/31/2017.
 */
@Deprecated
public class FilterTitleItem implements Parcelable {

    private String title;
    private boolean active;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
    }

    public FilterTitleItem() {
    }

    protected FilterTitleItem(Parcel in) {
        this.title = in.readString();
        this.active = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FilterTitleItem> CREATOR = new Parcelable.Creator<FilterTitleItem>() {
        @Override
        public FilterTitleItem createFromParcel(Parcel source) {
            return new FilterTitleItem(source);
        }

        @Override
        public FilterTitleItem[] newArray(int size) {
            return new FilterTitleItem[size];
        }
    };
}
