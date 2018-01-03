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
    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("mandatory_dob")
    @Expose
    private boolean mandatoryDob;

    protected Attributes(Parcel in) {
        name = in.readString();
        shortName = in.readString();
        logo = in.readString();
        mandatoryDob = in.readByte() != 0;
    }

    public Attributes() {
    }

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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(shortName);
        parcel.writeString(logo);
        parcel.writeByte((byte) (mandatoryDob ? 1 : 0));
    }

    public boolean isMandatoryDob() {
        return mandatoryDob;
    }
}
