package com.tokopedia.transaction.checkout.data.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class CartDataListResponse {

    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("is_coupon_active")
    @Expose
    private int isCouponActive;
    @SerializedName("max_quantity")
    @Expose
    private int maxQuantity;
    @SerializedName("max_char_note")
    @Expose
    private int maxCharNote;
    @SerializedName("messages")
    @Expose
    private Messages messages;
    @SerializedName("promo_suggestion")
    @Expose
    private PromoSuggestion promoSuggestion;
    @SerializedName("cart_list")
    @Expose
    private List<CartList> cartList = new ArrayList<>();

    public int getIsCouponActive() {
        return isCouponActive;
    }

    public List<String> getErrors() {
        return errors;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getMaxCharNote() {
        return maxCharNote;
    }

    public Messages getMessages() {
        return messages;
    }

    public PromoSuggestion getPromoSuggestion() {
        return promoSuggestion;
    }

    public List<CartList> getCartList() {
        return cartList;
    }
}
