package com.tokopedia.flight.airline.data.cloud.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/30/2017.
 */

public class AirlineData implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public String getId() {
        return id;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.attributes, flags);
    }

    public AirlineData() {
    }

    protected AirlineData(Parcel in) {
        this.id = in.readString();
        this.attributes = in.readParcelable(Attributes.class.getClassLoader());
    }

    public static final Parcelable.Creator<AirlineData> CREATOR = new Parcelable.Creator<AirlineData>() {
        @Override
        public AirlineData createFromParcel(Parcel source) {
            return new AirlineData(source);
        }

        @Override
        public AirlineData[] newArray(int size) {
            return new AirlineData[size];
        }
    };
}
