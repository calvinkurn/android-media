
package com.tokopedia.inbox.inboxmessage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserReputation implements Parcelable{

    @SerializedName("negative")
    @Expose
    String negative;
    @SerializedName("positive_percentage")
    @Expose
    String positivePercentage;
    @SerializedName("no_reputation")
    @Expose
    String noReputation;
    @SerializedName("neutral")
    @Expose
    String neutral;
    @SerializedName("positive")
    @Expose
    String positive;

    protected UserReputation(Parcel in) {
        negative = in.readString();
        positivePercentage = in.readString();
        noReputation = in.readString();
        neutral = in.readString();
        positive = in.readString();
    }

    public static final Creator<UserReputation> CREATOR = new Creator<UserReputation>() {
        @Override
        public UserReputation createFromParcel(Parcel in) {
            return new UserReputation(in);
        }

        @Override
        public UserReputation[] newArray(int size) {
            return new UserReputation[size];
        }
    };

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getPositivePercentage() {
        return positivePercentage  + " %";
    }

    public void setPositivePercentage(String positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    public String getNoReputation() {
        return noReputation;
    }

    public void setNoReputation(String noReputation) {
        this.noReputation = noReputation;
    }

    public String getNeutral() {
        return neutral;
    }

    public void setNeutral(String neutral) {
        this.neutral = neutral;
    }

    public String getPositive() {
        return positive;
    }

    public void setPositive(String positive) {
        this.positive = positive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(negative);
        dest.writeString(positivePercentage);
        dest.writeString(noReputation);
        dest.writeString(neutral);
        dest.writeString(positive);
    }
}
