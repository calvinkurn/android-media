
package com.tokopedia.seller.shopscore.data.source.cloud.model.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopScoreDetailServiceModel {

    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("data")
    @Expose
    private List<ShopScoreDetailDataServiceModel> data = null;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public List<ShopScoreDetailDataServiceModel> getData() {
        return data;
    }

    public void setData(List<ShopScoreDetailDataServiceModel> data) {
        this.data = data;
    }

}
