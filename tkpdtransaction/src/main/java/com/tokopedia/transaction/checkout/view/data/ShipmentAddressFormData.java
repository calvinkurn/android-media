package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class ShipmentAddressFormData implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ShipmentAddressFormData() {
    }

    protected ShipmentAddressFormData(Parcel in) {
    }

    public static final Parcelable.Creator<ShipmentAddressFormData> CREATOR = new Parcelable.Creator<ShipmentAddressFormData>() {
        @Override
        public ShipmentAddressFormData createFromParcel(Parcel source) {
            return new ShipmentAddressFormData(source);
        }

        @Override
        public ShipmentAddressFormData[] newArray(int size) {
            return new ShipmentAddressFormData[size];
        }
    };
}
