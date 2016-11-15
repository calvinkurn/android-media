package com.tokopedia.transaction.cart.model;


import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;

/**
 * @author anggaprasetiyo on 11/10/16.
 */

public class CartProductItemEditable {
    private boolean isEditMode;
    private CartProduct cartProduct;
    private String tempQuantity;
    private String tempNotes;
    private ProductEditData productEditData;

    public CartProductItemEditable(CartProduct cartProduct) {
        this.cartProduct = cartProduct;
        this.isEditMode = false;
        this.tempNotes = cartProduct.getProductNotes();
        this.tempQuantity = cartProduct.getProductQuantity() + "";
        this.productEditData = ProductEditData.initInstance(cartProduct);
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

    public ProductEditData getProductEditData() {
        return productEditData;
    }

    public void setProductEditData(ProductEditData productEditData) {
        this.productEditData = productEditData;
    }
}
