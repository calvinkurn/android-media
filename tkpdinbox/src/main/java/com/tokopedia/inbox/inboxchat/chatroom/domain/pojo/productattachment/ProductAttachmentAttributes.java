
package com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.productattachment;

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

    public ProductProfile getProductProfile() {
        return productProfile;
    }

}
