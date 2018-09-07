package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class StatusData implements Parcelable {

    public static final Parcelable.Creator<StatusData> CREATOR = new Parcelable.Creator<StatusData>() {
        @Override
        public StatusData createFromParcel(Parcel source) {
            return new StatusData(source);
        }

        @Override
        public StatusData[] newArray(int size) {
            return new StatusData[size];
        }
    };
    private int id;
    private String name;

    public StatusData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected StatusData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
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
}
