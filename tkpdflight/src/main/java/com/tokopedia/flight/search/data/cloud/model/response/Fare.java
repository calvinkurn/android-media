package com.tokopedia.flight.search.data.cloud.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/8/2017.
 */

public class Fare implements Parcelable {
    @SerializedName("adult")
    @Expose
    private String adult;
    @SerializedName("child")
    @Expose
    private String child;
    @SerializedName("infant")
    @Expose
    private String infant;
    @SerializedName("adult_numeric")
    @Expose
    private int adultNumeric;
    @SerializedName("child_numeric")
    @Expose
    private int childNumeric;
    @SerializedName("infant_numeric")
    @Expose
    private int infantNumeric;

    public Fare(String adult, String child, String infant, int adultNumeric, int childNumeric, int infantNumeric) {
        this.adult = adult;
        this.child = child;
        this.infant = infant;
        this.adultNumeric = adultNumeric;
        this.childNumeric = childNumeric;
        this.infantNumeric = infantNumeric;
    }

    public String getAdult() {
        return adult;
    }

    public String getChild() {
        return child;
    }

    public String getInfant() {
        return infant;
    }

    public int getAdultNumeric() {
        return adultNumeric;
    }

    public int getChildNumeric() {
        return childNumeric;
    }

    public int getInfantNumeric() {
        return infantNumeric;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adult);
        dest.writeString(this.child);
        dest.writeString(this.infant);
        dest.writeInt(this.adultNumeric);
        dest.writeInt(this.childNumeric);
        dest.writeInt(this.infantNumeric);
    }

    public Fare() {
    }

    protected Fare(Parcel in) {
        this.adult = in.readString();
        this.child = in.readString();
        this.infant = in.readString();
        this.adultNumeric = in.readInt();
        this.childNumeric = in.readInt();
        this.infantNumeric = in.readInt();
    }

    public static final Parcelable.Creator<Fare> CREATOR = new Parcelable.Creator<Fare>() {
        @Override
        public Fare createFromParcel(Parcel source) {
            return new Fare(source);
        }

        @Override
        public Fare[] newArray(int size) {
            return new Fare[size];
        }
    };
}
