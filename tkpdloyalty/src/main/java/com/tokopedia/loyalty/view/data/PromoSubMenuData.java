package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 05/01/18.
 */

public class PromoSubMenuData implements Parcelable {

    private String id;
    private String title;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public PromoSubMenuData() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected PromoSubMenuData(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<PromoSubMenuData> CREATOR = new Creator<PromoSubMenuData>() {
        @Override
        public PromoSubMenuData createFromParcel(Parcel source) {
            return new PromoSubMenuData(source);
        }

        @Override
        public PromoSubMenuData[] newArray(int size) {
            return new PromoSubMenuData[size];
        }
    };
}
