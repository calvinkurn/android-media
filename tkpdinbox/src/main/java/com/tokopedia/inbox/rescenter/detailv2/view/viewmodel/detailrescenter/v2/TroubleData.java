package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 08/11/17.
 */
public class TroubleData implements Parcelable {

    public static final Parcelable.Creator<TroubleData> CREATOR = new Parcelable.Creator<TroubleData>() {
        @Override
        public TroubleData createFromParcel(Parcel source) {
            return new TroubleData(source);
        }

        @Override
        public TroubleData[] newArray(int size) {
            return new TroubleData[size];
        }
    };
    private int id;
    private String name;

    public TroubleData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected TroubleData(Parcel in) {
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
