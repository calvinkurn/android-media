package com.tokopedia.transaction.cart.model.cartdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 5/8/17. Tokopedia
 */

public class CartItemRatesPrice implements Parcelable {

    private String insurancePrice = "0";

    private String shippingPrice = "0";

    private String cartSubTotal = "0";

    public CartItemRatesPrice() {

    }

    protected CartItemRatesPrice(Parcel in) {
        insurancePrice = in.readString();
        shippingPrice = in.readString();
        cartSubTotal = in.readString();
    }

    public static final Creator<CartItemRatesPrice> CREATOR = new Creator<CartItemRatesPrice>() {
        @Override
        public CartItemRatesPrice createFromParcel(Parcel in) {
            return new CartItemRatesPrice(in);
        }

        @Override
        public CartItemRatesPrice[] newArray(int size) {
            return new CartItemRatesPrice[size];
        }
    };

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(String shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getCartSubTotal() {
        return cartSubTotal;
    }

    public void setCartSubTotal(String cartSubTotal) {
        this.cartSubTotal = cartSubTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(insurancePrice);
        dest.writeString(shippingPrice);
        dest.writeString(cartSubTotal);
    }
}
