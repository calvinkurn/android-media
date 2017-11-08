package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class ShippingData implements Parcelable {
    private int id;
    private String name;

    public ShippingData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected ShippingData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ShippingData> CREATOR = new Parcelable.Creator<ShippingData>() {
        @Override
        public ShippingData createFromParcel(Parcel source) {
            return new ShippingData(source);
        }

        @Override
        public ShippingData[] newArray(int size) {
            return new ShippingData[size];
        }
    };
}
