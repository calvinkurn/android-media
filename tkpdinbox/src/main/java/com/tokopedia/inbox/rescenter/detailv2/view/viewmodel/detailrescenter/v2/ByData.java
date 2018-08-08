package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class ByData implements Parcelable {

    public static final Parcelable.Creator<ByData> CREATOR = new Parcelable.Creator<ByData>() {
        @Override
        public ByData createFromParcel(Parcel source) {
            return new ByData(source);
        }

        @Override
        public ByData[] newArray(int size) {
            return new ByData[size];
        }
    };
    private int id;
    private String name;
    private PictureData picture;
    private RoleData role;

    public ByData(int id, String name, PictureData picture, RoleData role) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.role = role;
    }

    protected ByData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.picture = in.readParcelable(PictureData.class.getClassLoader());
        this.role = in.readParcelable(RoleData.class.getClassLoader());
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

    public RoleData getRole() {
        return role;
    }

    public void setRole(RoleData role) {
        this.role = role;
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
        dest.writeParcelable(this.role, flags);
    }
}
