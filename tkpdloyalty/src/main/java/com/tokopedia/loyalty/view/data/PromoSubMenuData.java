package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 05/01/18.
 */

public class PromoSubMenuData implements Parcelable {

    private String id;
    private String title;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
    }

    public PromoSubMenuData() {
    }

    protected PromoSubMenuData(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<PromoSubMenuData> CREATOR = new Parcelable.Creator<PromoSubMenuData>() {
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
