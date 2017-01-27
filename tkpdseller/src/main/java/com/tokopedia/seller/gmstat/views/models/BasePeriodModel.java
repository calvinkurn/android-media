package com.tokopedia.seller.gmstat.views.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class BasePeriodModel implements Parcelable {
    public int type = -1;

    public BasePeriodModel(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
    }

    protected BasePeriodModel(Parcel in) {
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<BasePeriodModel> CREATOR = new Parcelable.Creator<BasePeriodModel>() {
        @Override
        public BasePeriodModel createFromParcel(Parcel source) {
            return new BasePeriodModel(source);
        }

        @Override
        public BasePeriodModel[] newArray(int size) {
            return new BasePeriodModel[size];
        }
    };
}
