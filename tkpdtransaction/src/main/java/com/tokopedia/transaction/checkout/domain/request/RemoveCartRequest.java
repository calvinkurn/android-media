package com.tokopedia.transaction.checkout.domain.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author anggaprasetiyo on 20/02/18.
 */

public class RemoveCartRequest {
    @SerializedName("cart_ids")
    @Expose
    private List<Integer> cartIds = null;
    @SerializedName("add_wishlist")
    @Expose
    private int addWishlist;

    private RemoveCartRequest(Builder builder) {
        setCartIds(builder.cartIds);
        setAddWishlist(builder.addWishlist);
    }

    public List<Integer> getCartIds() {
        return cartIds;
    }

    public void setCartIds(List<Integer> cartIds) {
        this.cartIds = cartIds;
    }

    public int getAddWishlist() {
        return addWishlist;
    }

    public void setAddWishlist(int addWishlist) {
        this.addWishlist = addWishlist;
    }


    public static final class Builder {
        private List<Integer> cartIds;
        private int addWishlist;

        public Builder() {
        }

        public Builder cartIds(List<Integer> val) {
            cartIds = val;
            return this;
        }

        public Builder addWishlist(int val) {
            addWishlist = val;
            return this;
        }

        public RemoveCartRequest build() {
            return new RemoveCartRequest(this);
        }
    }
}
