
package com.tokopedia.transaction.purchase.detail.model.buyagain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("cart_id")
    @Expose
    private int cartId;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("customer_id")
    @Expose
    private int customerId;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

}
