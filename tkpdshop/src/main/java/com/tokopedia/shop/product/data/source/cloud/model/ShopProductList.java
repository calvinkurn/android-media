
package com.tokopedia.shop.product.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopProductList {

    @SerializedName("total_data")
    @Expose
    private long totalData;
    @SerializedName("list")
    @Expose
    private java.util.List<ShopProduct> list = null;

    public long getTotalData() {
        return totalData;
    }

    public void setTotalData(long totalData) {
        this.totalData = totalData;
    }

    public java.util.List<ShopProduct> getList() {
        return list;
    }

    public void setList(java.util.List<ShopProduct> list) {
        this.list = list;
    }

}
