package com.tokopedia.transaction.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class DropshipperShippingOptionModel implements Parcelable {

    private boolean isDropshipping;

    public DropshipperShippingOptionModel() {

    }

    protected DropshipperShippingOptionModel(Parcel in) {
        isDropshipping = in.readByte() != 0;
    }

    public static final Creator<DropshipperShippingOptionModel> CREATOR = new Creator<DropshipperShippingOptionModel>() {
        @Override
        public DropshipperShippingOptionModel createFromParcel(Parcel in) {
            return new DropshipperShippingOptionModel(in);
        }

        @Override
        public DropshipperShippingOptionModel[] newArray(int size) {
            return new DropshipperShippingOptionModel[size];
        }
    };

    public boolean isDropshipping() {
        return isDropshipping;
    }

    public void setDropshipping(boolean dropshipping) {
        isDropshipping = dropshipping;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isDropshipping ? 1 : 0));
    }
}
