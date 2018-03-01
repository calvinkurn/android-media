package com.tokopedia.transaction.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 20/02/18
 */

public class CheckedCartItemData implements Parcelable {

    private boolean isChecked;

    private CartItemData cartItemData;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public CartItemData getCartItemData() {
        return cartItemData;
    }

    public void setCartItemData(CartItemData cartItemData) {
        this.cartItemData = cartItemData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.cartItemData, flags);
    }

    public CheckedCartItemData() {
    }

    public CheckedCartItemData(boolean isChecked, CartItemData cartItemData) {
        this.isChecked = isChecked;
        this.cartItemData = cartItemData;
    }

    protected CheckedCartItemData(Parcel in) {
        this.isChecked = in.readByte() != 0;
        this.cartItemData = in.readParcelable(CartItemData.class.getClassLoader());
    }

    public static final Creator<CheckedCartItemData> CREATOR = new Creator<CheckedCartItemData>() {
        @Override
        public CheckedCartItemData createFromParcel(Parcel source) {
            return new CheckedCartItemData(source);
        }

        @Override
        public CheckedCartItemData[] newArray(int size) {
            return new CheckedCartItemData[size];
        }
    };
}
