package com.tokopedia.transaction.checkout.view.holderitemdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartDeleteItemHolderData implements Parcelable {

    private CartItemData cartItemData;
    private boolean isSelected;

    public CartItemData getCartItemData() {
        return cartItemData;
    }

    public void setCartItemData(CartItemData cartItemData) {
        this.cartItemData = cartItemData;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.cartItemData, flags);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public CartDeleteItemHolderData() {
    }

    protected CartDeleteItemHolderData(Parcel in) {
        this.cartItemData = in.readParcelable(CartItemData.class.getClassLoader());
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CartDeleteItemHolderData> CREATOR = new Parcelable.Creator<CartDeleteItemHolderData>() {
        @Override
        public CartDeleteItemHolderData createFromParcel(Parcel source) {
            return new CartDeleteItemHolderData(source);
        }

        @Override
        public CartDeleteItemHolderData[] newArray(int size) {
            return new CartDeleteItemHolderData[size];
        }
    };
}
