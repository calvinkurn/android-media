package com.tokopedia.transaction.cart.model;

import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartItemEditable {

    public static final int ERROR_NON = 0;
    public static final int ERROR_DROPSHIPPER_NAME = 1;
    public static final int ERROR_DROPSHIPPER_PHONE = 2;
    public static final int ERROR_CART_CANNOT_PROCESS = 3;

    private boolean isEditMode;
    private TransactionList transactionList;

    private boolean isDropShipper;
    private boolean isPartialDeliver;

    private String dropShipperName;
    private String dropShipperPhone;
    private String cartString;
    private int errorType;

    public CartItemEditable(TransactionList data) {
        this.transactionList = data;
        this.isEditMode = false;
        this.isDropShipper = false;
        this.isPartialDeliver = false;
        this.dropShipperName = "";
        this.dropShipperPhone = "";
        this.cartString = "";
        this.errorType = data.getCartCanProcess() == 0 ? ERROR_CART_CANNOT_PROCESS : ERROR_NON;
    }

    public CartItemEditable finalizeAllData() {
        if (this.getTransactionList().getCartCanProcess() == 0) {
            errorType = ERROR_CART_CANNOT_PROCESS;
            return this;
        }
        if (this.isDropShipper() && (this.dropShipperName == null || this.dropShipperName.isEmpty())) {
            errorType = ERROR_DROPSHIPPER_NAME;
            return this;
        }
        if (this.isDropShipper() && (this.dropShipperPhone == null || this.dropShipperPhone.isEmpty())) {
            errorType = ERROR_DROPSHIPPER_PHONE;
            return this;
        }
        return this;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public TransactionList getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(TransactionList transactionList) {
        this.transactionList = transactionList;
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

    public String getCartString() {
        return cartString;
    }

    public void setCartString(String cartString) {
        this.cartString = cartString;
    }
}
