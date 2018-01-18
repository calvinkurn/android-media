package com.tokopedia.transaction.checkout.view.holderitemdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.view.data.CartItemData;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartItemHolderData implements Parcelable {
    public static final int ERROR_SHOP_CLOSED = 0;
    public static final int ERROR_PRODUCT_NOT_AVAILABLE = 1;
    public static final int ERROR_STOCK = 2;

    private CartItemData cartItemData;
    private boolean isErrorItem;
    private int errorType;
    private String messageError;
    private boolean editableRemark;

    public CartItemData getCartItemData() {
        return cartItemData;
    }

    public void setCartItemData(CartItemData cartItemData) {
        this.cartItemData = cartItemData;
    }

    public boolean isErrorItem() {
        return isErrorItem;
    }

    public void setErrorItem(boolean errorItem) {
        isErrorItem = errorItem;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public boolean isEditableRemark() {
        return editableRemark;
    }

    public void setEditableRemark(boolean editableRemark) {
        this.editableRemark = editableRemark;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.cartItemData, flags);
        dest.writeByte(this.isErrorItem ? (byte) 1 : (byte) 0);
        dest.writeInt(this.errorType);
        dest.writeString(this.messageError);
        dest.writeByte(this.editableRemark ? (byte) 1 : (byte) 0);
    }

    public CartItemHolderData() {
    }

    protected CartItemHolderData(Parcel in) {
        this.cartItemData = in.readParcelable(CartItemData.class.getClassLoader());
        this.isErrorItem = in.readByte() != 0;
        this.errorType = in.readInt();
        this.messageError = in.readString();
        this.editableRemark = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CartItemHolderData> CREATOR = new Parcelable.Creator<CartItemHolderData>() {
        @Override
        public CartItemHolderData createFromParcel(Parcel source) {
            return new CartItemHolderData(source);
        }

        @Override
        public CartItemHolderData[] newArray(int size) {
            return new CartItemHolderData[size];
        }
    };
}
