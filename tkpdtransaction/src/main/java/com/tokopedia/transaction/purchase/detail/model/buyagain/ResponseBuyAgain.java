package com.tokopedia.transaction.purchase.detail.model.buyagain;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBuyAgain {

    @SerializedName("add_to_cart_multi")
    @Expose
    private AddToCartMulti addToCartMulti;

    public AddToCartMulti getAddToCartMulti() {
        return addToCartMulti;
    }

    public void setAddToCartMulti(AddToCartMulti addToCartMulti) {
        this.addToCartMulti = addToCartMulti;
    }
}
