
package com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.productattachment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductProfile {

    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("url")
    @Expose
    private String url;

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
