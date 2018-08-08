
package com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("product_returnable")
    @Expose
    private int productReturnable;
    @SerializedName("shop_has_terms")
    @Expose
    private int shopHasTerms;

    public int getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(int productReturnable) {
        this.productReturnable = productReturnable;
    }

    public int getShopHasTerms() {
        return shopHasTerms;
    }

    public void setShopHasTerms(int shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }

}
