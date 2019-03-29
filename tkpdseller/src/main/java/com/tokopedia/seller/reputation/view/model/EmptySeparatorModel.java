package com.tokopedia.seller.reputation.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.base.list.seller.common.util.ItemType;

/**
 * @author normansyahputa on 3/27/17.
 */

public class EmptySeparatorModel implements Parcelable, ItemType {
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

    protected EmptySeparatorModel(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
