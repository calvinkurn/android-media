package com.tokopedia.transaction.checkout.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 15/02/18.
 */

public class CartListData implements Parcelable {
    private boolean isError;
    private String errorMessage;

    private List<CartItemData> cartItemDataList = new ArrayList<>();
    private CartPromoSuggestion cartPromoSuggestion;

    public List<CartItemData> getCartItemDataList() {
        return cartItemDataList;
    }

    public void setCartItemDataList(List<CartItemData> cartItemDataList) {
        this.cartItemDataList = cartItemDataList;
    }

    public CartPromoSuggestion getCartPromoSuggestion() {
        return cartPromoSuggestion;
    }

    public void setCartPromoSuggestion(CartPromoSuggestion cartPromoSuggestion) {
        this.cartPromoSuggestion = cartPromoSuggestion;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public CartListData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeString(this.errorMessage);
        dest.writeTypedList(this.cartItemDataList);
        dest.writeParcelable(this.cartPromoSuggestion, flags);
    }

    protected CartListData(Parcel in) {
        this.isError = in.readByte() != 0;
        this.errorMessage = in.readString();
        this.cartItemDataList = in.createTypedArrayList(CartItemData.CREATOR);
        this.cartPromoSuggestion = in.readParcelable(CartPromoSuggestion.class.getClassLoader());
    }

    public static final Creator<CartListData> CREATOR = new Creator<CartListData>() {
        @Override
        public CartListData createFromParcel(Parcel source) {
            return new CartListData(source);
        }

        @Override
        public CartListData[] newArray(int size) {
            return new CartListData[size];
        }
    };
}
