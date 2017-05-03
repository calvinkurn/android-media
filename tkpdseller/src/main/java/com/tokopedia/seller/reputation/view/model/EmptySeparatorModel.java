package com.tokopedia.seller.reputation.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.seller.topads.view.model.TypeBasedModel;

/**
 * @author normansyahputa on 3/27/17.
 */

public class EmptySeparatorModel extends TypeBasedModel implements Parcelable {
    public static final int TYPE = 12118412;
    public static final Parcelable.Creator<EmptySeparatorModel> CREATOR = new Parcelable.Creator<EmptySeparatorModel>() {
        @Override
        public EmptySeparatorModel createFromParcel(Parcel source) {
            return new EmptySeparatorModel(source);
        }

        @Override
        public EmptySeparatorModel[] newArray(int size) {
            return new EmptySeparatorModel[size];
        }
    };

    public EmptySeparatorModel() {
        super(TYPE);
    }

    protected EmptySeparatorModel(Parcel in) {
        this();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
