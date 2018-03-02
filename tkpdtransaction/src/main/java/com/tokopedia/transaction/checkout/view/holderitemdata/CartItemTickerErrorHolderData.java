package com.tokopedia.transaction.checkout.view.holderitemdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartTickerErrorData;

/**
 * @author anggaprasetiyo on 02/03/18.
 */

public class CartItemTickerErrorHolderData implements Parcelable {
    private CartTickerErrorData cartTickerErrorData;

    private CartItemTickerErrorHolderData(Builder builder) {
        cartTickerErrorData = builder.cartTickerErrorData;
    }

    public static final class Builder {
        private CartTickerErrorData cartTickerErrorData;

        public Builder() {
        }

        public Builder cartTickerErrorData(CartTickerErrorData val) {
            cartTickerErrorData = val;
            return this;
        }

        public CartItemTickerErrorHolderData build() {
            return new CartItemTickerErrorHolderData(this);
        }
    }

    public CartTickerErrorData getCartTickerErrorData() {
        return cartTickerErrorData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.cartTickerErrorData, flags);
    }

    protected CartItemTickerErrorHolderData(Parcel in) {
        this.cartTickerErrorData = in.readParcelable(CartTickerErrorData.class.getClassLoader());
    }

    public static final Parcelable.Creator<CartItemTickerErrorHolderData> CREATOR = new Parcelable.Creator<CartItemTickerErrorHolderData>() {
        @Override
        public CartItemTickerErrorHolderData createFromParcel(Parcel source) {
            return new CartItemTickerErrorHolderData(source);
        }

        @Override
        public CartItemTickerErrorHolderData[] newArray(int size) {
            return new CartItemTickerErrorHolderData[size];
        }
    };
}
