package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.view.data.cartshipmentform.CartShipmentAddressFormData;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class UpdateCartListData implements Parcelable {

    private UpdateCartData updateCartData;
    private CartListData cartListData;
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

    public CartListData getCartListData() {
        return cartListData;
    }

    public void setCartListData(CartListData cartListData) {
        this.cartListData = cartListData;
    }


    public UpdateCartListData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.updateCartData, flags);
        dest.writeParcelable(this.cartListData, flags);
        dest.writeParcelable(this.shipmentAddressFormData, flags);
    }

    protected UpdateCartListData(Parcel in) {
        this.updateCartData = in.readParcelable(UpdateCartData.class.getClassLoader());
        this.cartListData = in.readParcelable(CartListData.class.getClassLoader());
        this.shipmentAddressFormData = in.readParcelable(CartShipmentAddressFormData.class.getClassLoader());
    }

    public static final Creator<UpdateCartListData> CREATOR = new Creator<UpdateCartListData>() {
        @Override
        public UpdateCartListData createFromParcel(Parcel source) {
            return new UpdateCartListData(source);
        }

        @Override
        public UpdateCartListData[] newArray(int size) {
            return new UpdateCartListData[size];
        }
    };
}
