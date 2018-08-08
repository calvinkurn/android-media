package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class CustomerData implements Parcelable {

    public static final Parcelable.Creator<CustomerData> CREATOR = new Parcelable.Creator<CustomerData>() {
        @Override
        public CustomerData createFromParcel(Parcel source) {
            return new CustomerData(source);
        }

        @Override
        public CustomerData[] newArray(int size) {
            return new CustomerData[size];
        }
    };
    private int id;
    private String name;
    private PictureData picture;

    public CustomerData(int id, String name, PictureData picture) {
        this.id = id;
        this.name = name;
        this.picture = picture;
    }

    protected CustomerData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.picture = in.readParcelable(PictureData.class.getClassLoader());
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

    public PictureData getPicture() {
        return picture;
    }

    public void setPicture(PictureData picture) {
        this.picture = picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.picture, flags);
    }
}
