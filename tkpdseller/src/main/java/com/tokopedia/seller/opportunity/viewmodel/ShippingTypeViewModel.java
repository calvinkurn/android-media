package com.tokopedia.seller.opportunity.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nisie on 3/6/17.
 */

public class ShippingTypeViewModel implements Parcelable{
    int shippingTypeId;
    String shippingTypeName;

    public ShippingTypeViewModel() {
    }

    public ShippingTypeViewModel(String shippingTypeName, int shippingTypeId) {
        this.shippingTypeName = shippingTypeName;
        this.shippingTypeId = shippingTypeId;
    }

    protected ShippingTypeViewModel(Parcel in) {
        shippingTypeId = in.readInt();
        shippingTypeName = in.readString();
    }

    public static final Creator<ShippingTypeViewModel> CREATOR = new Creator<ShippingTypeViewModel>() {
        @Override
        public ShippingTypeViewModel createFromParcel(Parcel in) {
            return new ShippingTypeViewModel(in);
        }

        @Override
        public ShippingTypeViewModel[] newArray(int size) {
            return new ShippingTypeViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shippingTypeId);
        dest.writeString(shippingTypeName);
    }
}
