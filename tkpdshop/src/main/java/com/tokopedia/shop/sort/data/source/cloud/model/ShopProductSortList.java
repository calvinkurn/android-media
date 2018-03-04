package com.tokopedia.shop.sort.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 3/4/18.
 */

public class ShopProductSortList {

    @SerializedName("sort")
    @Expose
    List<ShopProductSort> sort = new ArrayList<>();

    public List<ShopProductSort> getSort() {
        return sort;
    }

    public void setSort(List<ShopProductSort> sort) {
        this.sort = sort;
    }
}
