
package com.tokopedia.inbox.inboxchat.domain.pojo.productattachment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductAttachmentAttributes {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_profile")
    @Expose
    private ProductProfile productProfile;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ProductProfile getProductProfile() {
        return productProfile;
    }

    public void setProductProfile(ProductProfile productProfile) {
        this.productProfile = productProfile;
    }

}
