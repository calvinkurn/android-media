package com.tokopedia.transaction.checkout.view.holderitemdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.transaction.checkout.view.data.CartItemData;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartItemHolderData implements Parcelable {
    public static final int ERROR_FIELD_BETWEEN = 1;
    public static final int ERROR_FIELD_MAX_CHAR = 2;
    public static final int ERROR_FIELD_REQUIRED = 3;
    public static final int ERROR_FIELD_AVAILABLE_STOCK = 4;
    public static final int ERROR_PRODUCT_MAX_QUANTITY = 5;
    public static final int ERROR_PRODUCT_MIN_QUANTITY = 6;
    public static final int ERROR_ADDITIONAL = 7;
    public static final int ERROR_EMPTY = 0;

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
        if (cartItemData.getUpdatedData().getRemark().length() > 140) {
            this.messageError = cartItemData.getErrorData().getErrorFieldMaxChar()
                    .replace("{{value}}", String.valueOf(cartItemData.getUpdatedData().getMaxCharRemark()));
            return ERROR_FIELD_MAX_CHAR;
        } else if (cartItemData.getUpdatedData().getQuantity() > cartItemData.getUpdatedData().getMaxQuantity()) {
            this.messageError = cartItemData.getErrorData().getErrorProductMaxQuantity()
                    .replace("{{value}}", String.valueOf(cartItemData.getUpdatedData().getMaxQuantity()));
            return ERROR_PRODUCT_MAX_QUANTITY;
        } else if (cartItemData.getUpdatedData().getQuantity() < cartItemData.getOriginData().getMinimalQtyOrder()) {
            this.messageError = cartItemData.getErrorData().getErrorProductMinQuantity()
                    .replace("{{value}}", String.valueOf(cartItemData.getOriginData().getMinimalQtyOrder()));
            return ERROR_PRODUCT_MIN_QUANTITY;
        } else if (!cartItemData.getErrorData().getErrorAdditional().isEmpty()) {
            this.messageError = cartItemData.getErrorData().getErrorAdditional().get(0);
            return ERROR_ADDITIONAL;
        } else {
            this.messageError = "";
            return ERROR_EMPTY;
        }

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


    public CartItemHolderData() {
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

    protected CartItemHolderData(Parcel in) {
        this.cartItemData = in.readParcelable(CartItemData.class.getClassLoader());
        this.isErrorItem = in.readByte() != 0;
        this.errorType = in.readInt();
        this.messageError = in.readString();
        this.editableRemark = in.readByte() != 0;
    }

    public static final Creator<CartItemHolderData> CREATOR = new Creator<CartItemHolderData>() {
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
