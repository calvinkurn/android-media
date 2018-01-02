package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class CreateByData implements Parcelable {

    public static final Parcelable.Creator<CreateByData> CREATOR = new Parcelable.Creator<CreateByData>() {
        @Override
        public CreateByData createFromParcel(Parcel source) {
            return new CreateByData(source);
        }

        @Override
        public CreateByData[] newArray(int size) {
            return new CreateByData[size];
        }
    };
    private int id;
    private String name;
    private PictureData picture;

    public CreateByData(int id, String name, PictureData picture) {
        this.id = id;
        this.name = name;
        this.picture = picture;
    }

    protected CreateByData(Parcel in) {
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
