package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 24/01/18.
 */
public class ProductImageResponse {
    /**
     * thumb : https://imagerouter.tokopedia.com/img/300/product-1/2017/6/6/9539418/9539418_6877d7cd-69e5-42d3-b161-2d11b3b82d9e_480_480.jpg
     * full : https://imagerouter.tokopedia.com/img/700/product-1/2017/6/6/9539418/9539418_6877d7cd-69e5-42d3-b161-2d11b3b82d9e_480_480.jpg
     */

    @SerializedName("thumb")
    private String thumb;
    @SerializedName("full")
    private String full;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }
}
