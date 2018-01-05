package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class PromoMenuData implements Parcelable {
    private String title;
    private String menuId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.menuId);
    }

    public PromoMenuData() {
    }

    protected PromoMenuData(Parcel in) {
        this.title = in.readString();
        this.menuId = in.readString();
    }

    public static final Parcelable.Creator<PromoMenuData> CREATOR = new Parcelable.Creator<PromoMenuData>() {
        @Override
        public PromoMenuData createFromParcel(Parcel source) {
            return new PromoMenuData(source);
        }

        @Override
        public PromoMenuData[] newArray(int size) {
            return new PromoMenuData[size];
        }
    };
}
