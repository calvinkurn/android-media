package com.tokopedia.tkpdtrain.homepage.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizky on 21/02/18.
 */

public class KAIPassengerViewModel implements Parcelable, Cloneable {

    private int adult;
    private int infant;

    public KAIPassengerViewModel(Parcel in) {
        adult = in.readInt();
        infant = in.readInt();
    }

    public KAIPassengerViewModel(int adult, int infant) {
        this.adult = adult;
        this.infant = infant;
    }

    public static final Parcelable.Creator<KAIPassengerViewModel> CREATOR = new Parcelable.Creator<KAIPassengerViewModel>() {
        @Override
        public KAIPassengerViewModel createFromParcel(Parcel in) {
            return new KAIPassengerViewModel(in);
        }

        @Override
        public KAIPassengerViewModel[] newArray(int size) {
            return new KAIPassengerViewModel[size];
        }
    };

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adult);
        dest.writeInt(infant);
    }

    public static class Builder {
        private int adult;
        private int infant;

        public Builder() {
            adult = 0;
            infant = 0;
        }

        public Builder setAdult(int adult) {
            this.adult = adult;
            return this;
        }

        public Builder setInfant(int infant) {
            this.infant = infant;
            return this;
        }

        public KAIPassengerViewModel build() {
            return new KAIPassengerViewModel(adult, infant);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
