package com.tokopedia.transaction.cart.model;


import com.tokopedia.transaction.cart.model.cartdata.CartProduct;

/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartProductItemEditable {
    private boolean isEditMode;
    private CartProduct cartProduct;
    private String tempQuantity;
    private String tempNotes;

    public CartProductItemEditable(CartProduct cartProduct) {
        this.cartProduct = cartProduct;
        this.isEditMode = false;
        this.tempNotes = cartProduct.getProductNotes();
        this.tempQuantity = cartProduct.getProductQuantity() + "";
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public CartProduct getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(CartProduct cartProduct) {
        this.cartProduct = cartProduct;
    }

    public String getTempQuantity() {
        return tempQuantity;
    }

    public void setTempQuantity(String tempQuantity) {
        this.tempQuantity = tempQuantity;
    }

    public String getTempNotes() {
        return tempNotes;
    }

    public void setTempNotes(String tempNotes) {
        this.tempNotes = tempNotes;
    }
}
