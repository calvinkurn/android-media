package com.tokopedia.transaction.checkout.domain.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.ResetCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;

/**
 * @author anggaprasetiyo on 07/03/18.
 */

public class ResetAndShipmentFormCartData implements Parcelable {
    private ResetCartData resetCartData;
    private CartShipmentAddressFormData cartShipmentAddressFormData;


    public ResetCartData getResetCartData() {
        return resetCartData;
    }

    public void setResetCartData(ResetCartData resetCartData) {
        this.resetCartData = resetCartData;
    }

    public CartShipmentAddressFormData getCartShipmentAddressFormData() {
        return cartShipmentAddressFormData;
    }

    public void setCartShipmentAddressFormData(CartShipmentAddressFormData cartShipmentAddressFormData) {
        this.cartShipmentAddressFormData = cartShipmentAddressFormData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.resetCartData, flags);
        dest.writeParcelable(this.cartShipmentAddressFormData, flags);
    }

    public ResetAndShipmentFormCartData() {
    }

    protected ResetAndShipmentFormCartData(Parcel in) {
        this.resetCartData = in.readParcelable(ResetCartData.class.getClassLoader());
        this.cartShipmentAddressFormData = in.readParcelable(CartShipmentAddressFormData.class.getClassLoader());
    }

    public static final Parcelable.Creator<ResetAndShipmentFormCartData> CREATOR =
            new Parcelable.Creator<ResetAndShipmentFormCartData>() {
                @Override
                public ResetAndShipmentFormCartData createFromParcel(Parcel source) {
                    return new ResetAndShipmentFormCartData(source);
                }

                @Override
                public ResetAndShipmentFormCartData[] newArray(int size) {
                    return new ResetAndShipmentFormCartData[size];
                }
            };
}
