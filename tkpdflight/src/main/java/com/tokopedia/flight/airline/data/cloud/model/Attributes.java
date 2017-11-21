package com.tokopedia.flight.airline.data.cloud.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/30/2017.
 */

public class Attributes implements Parcelable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("logo")
    @Expose
    private String logo;

    public String getName() {
        if (TextUtils.isEmpty(shortName)) {
            return name;
        }
        return shortName;
    }

    public String getFullName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLogo() {
        return logo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.shortName);
        dest.writeString(this.logo);
    }

    public Attributes() {
    }

    protected Attributes(Parcel in) {
        this.name = in.readString();
        this.shortName = in.readString();
        this.logo = in.readString();
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel source) {
            return new Attributes(source);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };
}
