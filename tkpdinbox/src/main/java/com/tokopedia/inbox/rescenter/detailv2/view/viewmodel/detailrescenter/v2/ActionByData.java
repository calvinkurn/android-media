package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailrescenter.v2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfsx on 07/11/17.
 */
public class ActionByData implements Parcelable {

    public static final Parcelable.Creator<ActionByData> CREATOR = new Parcelable.Creator<ActionByData>() {
        @Override
        public ActionByData createFromParcel(Parcel source) {
            return new ActionByData(source);
        }

        @Override
        public ActionByData[] newArray(int size) {
            return new ActionByData[size];
        }
    };
    private int id;
    private String name;

    public ActionByData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected ActionByData(Parcel in) {
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
