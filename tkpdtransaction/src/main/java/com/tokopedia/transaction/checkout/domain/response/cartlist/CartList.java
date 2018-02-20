package com.tokopedia.transaction.checkout.domain.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CartList {

    @SerializedName("cart_id")
    @Expose
    private int cartId;
    @SerializedName("user_address_id")
    @Expose
    private int userAddressId;
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("product")
    @Expose
    private Product product;
    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("messages")
    @Expose
    private List<Object> messages = new ArrayList<>();

    public int getCartId() {
        return cartId;
    }

    public int getUserAddressId() {
        return userAddressId;
    }

    public Shop getShop() {
        return shop;
    }

    public Product getProduct() {
        return product;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<Object> getMessages() {
        return messages;
    }
}
