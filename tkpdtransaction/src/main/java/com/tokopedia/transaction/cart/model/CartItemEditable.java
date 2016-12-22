package com.tokopedia.transaction.cart.model;

import com.tokopedia.transaction.cart.model.cartdata.CartItem;

/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartItemEditable {

    public static final int ERROR_NON = 0;
    public static final int ERROR_DROPSHIPPER_NAME = 1;
    public static final int ERROR_DROPSHIPPER_PHONE = 2;
    public static final int ERROR_CART_CANNOT_PROCESS = 3;

    private boolean isEditMode;
    private CartItem cartItem;

    private boolean isDropShipper;
    private boolean isPartialDeliver;

    private String dropShipperName;
    private String dropShipperPhone;
    private String cartStringForDeliverOption;
    private String cartStringForDropShipperOption;
    private int errorType;

    public CartItemEditable(CartItem data) {
        this.cartItem = data;
        this.isEditMode = false;
        this.isDropShipper = false;
        this.isPartialDeliver = false;
        this.dropShipperName = "";
        this.dropShipperPhone = "";
        this.cartStringForDeliverOption = "";
        this.cartStringForDropShipperOption = "";
        this.errorType = data.getCartCanProcess() == 0 ? ERROR_CART_CANNOT_PROCESS : ERROR_NON;
    }

    public CartItemEditable finalizeAllData() {
        if (this.getCartItem().getCartCanProcess() == 0) {
            errorType = ERROR_CART_CANNOT_PROCESS;
            return this;
        } else if (this.isDropShipper() && (this.dropShipperName == null
                || this.dropShipperName.isEmpty())) {
            errorType = ERROR_DROPSHIPPER_NAME;
            return this;
        } else if (this.isDropShipper() && (this.dropShipperPhone == null
                || this.dropShipperPhone.isEmpty())) {
            errorType = ERROR_DROPSHIPPER_PHONE;
            return this;
        } else {
            errorType = ERROR_NON;
            return this;
        }
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public boolean isDropShipper() {
        return isDropShipper;
    }

    public void setDropShipper(boolean dropShipper) {
        isDropShipper = dropShipper;
    }

    public boolean isPartialDeliver() {
        return isPartialDeliver;
    }

    public void setPartialDeliver(boolean partialDeliver) {
        isPartialDeliver = partialDeliver;
    }

    public String getDropShipperName() {
        return dropShipperName;
    }

    public void setDropShipperName(String dropShipperName) {
        this.dropShipperName = dropShipperName;
    }

    public String getDropShipperPhone() {
        return dropShipperPhone;
    }

    public void setDropShipperPhone(String dropShipperPhone) {
        this.dropShipperPhone = dropShipperPhone;
    }

    public String getCartStringForDeliverOption() {
        return cartStringForDeliverOption;
    }

    public void setCartStringForDeliverOption(String cartStringForDeliverOption) {
        this.cartStringForDeliverOption = cartStringForDeliverOption;
    }

    public String getCartStringForDropShipperOption() {
        return cartStringForDropShipperOption;
    }

    public void setCartStringForDropShipperOption(String cartStringForDropShipperOption) {
        this.cartStringForDropShipperOption = cartStringForDropShipperOption;
    }
}
