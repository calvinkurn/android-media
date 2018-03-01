package com.tokopedia.transaction.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class UpdateToSingleAddressShipmentData implements Parcelable {

    private UpdateCartData updateCartData;
    private CartShipmentAddressFormData shipmentAddressFormData;

    public UpdateCartData getUpdateCartData() {
        return updateCartData;
    }

    public void setUpdateCartData(UpdateCartData updateCartData) {
        this.updateCartData = updateCartData;
    }

    public CartShipmentAddressFormData getShipmentAddressFormData() {
        return shipmentAddressFormData;
    }

    public void setShipmentAddressFormData(CartShipmentAddressFormData shipmentAddressFormData) {
        this.shipmentAddressFormData = shipmentAddressFormData;
    }


    public UpdateToSingleAddressShipmentData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.updateCartData, flags);
        dest.writeParcelable(this.shipmentAddressFormData, flags);
    }

    protected UpdateToSingleAddressShipmentData(Parcel in) {
        this.updateCartData = in.readParcelable(UpdateCartData.class.getClassLoader());
        this.shipmentAddressFormData = in.readParcelable(CartShipmentAddressFormData.class.getClassLoader());
    }

    public static final Creator<UpdateToSingleAddressShipmentData> CREATOR = new Creator<UpdateToSingleAddressShipmentData>() {
        @Override
        public UpdateToSingleAddressShipmentData createFromParcel(Parcel source) {
            return new UpdateToSingleAddressShipmentData(source);
        }

        @Override
        public UpdateToSingleAddressShipmentData[] newArray(int size) {
            return new UpdateToSingleAddressShipmentData[size];
        }
    };
}
