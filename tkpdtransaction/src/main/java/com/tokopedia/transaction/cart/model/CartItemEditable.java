package com.tokopedia.transaction.cart.model;

import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartItemEditable {
    private boolean isEditMode;
    private TransactionList transactionList;

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
}
