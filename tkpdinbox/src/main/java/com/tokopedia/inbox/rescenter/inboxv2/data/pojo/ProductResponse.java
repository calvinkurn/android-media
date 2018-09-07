package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 24/01/18.
 */
public class ProductResponse {
    /**
     * id : 651034
     * images : [{"thumb":"https://imagerouter.tokopedia.com/img/300/product-1/2017/6/6/9539418/9539418_6877d7cd-69e5-42d3-b161-2d11b3b82d9e_480_480.jpg","full":"https://imagerouter.tokopedia.com/img/700/product-1/2017/6/6/9539418/9539418_6877d7cd-69e5-42d3-b161-2d11b3b82d9e_480_480.jpg"},{"thumb":"https://imagerouter.tokopedia.com/img/300/product-1/2017/6/6/9539418/9539418_c7d034a4-ec88-4ad3-add1-f9dbcaa4cafe_450_450.jpg","full":"https://imagerouter.tokopedia.com/img/700/product-1/2017/6/6/9539418/9539418_c7d034a4-ec88-4ad3-add1-f9dbcaa4cafe_450_450.jpg"}]
     */

    @SerializedName("id")
    private int id;
    @SerializedName("images")
    private List<ProductImageResponse> images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ProductImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ProductImageResponse> images) {
        this.images = images;
    }

}
