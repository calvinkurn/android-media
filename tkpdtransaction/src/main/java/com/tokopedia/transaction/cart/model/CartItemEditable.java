package com.tokopedia.transaction.cart.model;

import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartItemEditable {
    private boolean isEditMode;
    private TransactionList transactionList;
    private String strDropShip;
    private String strPartialDeliver;

    public CartItemEditable(TransactionList data) {
        this.transactionList = data;
        this.isEditMode = false;
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

    public String getStrDropShip() {
        return strDropShip;
    }

    public void setStrDropShip(String strDropShip) {
        this.strDropShip = strDropShip;
    }

    public String getStrPartialDeliver() {
        return strPartialDeliver;
    }

    public void setStrPartialDeliver(String strPartialDeliver) {
        this.strPartialDeliver = strPartialDeliver;
    }
}
