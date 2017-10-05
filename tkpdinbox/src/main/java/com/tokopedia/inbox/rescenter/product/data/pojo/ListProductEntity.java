package com.tokopedia.inbox.rescenter.product.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 3/27/17.
 */

public class ListProductEntity {


    @SerializedName("listComplainProduct")
    private List<ListProductItem> listComplainProduct;

    public List<ListProductItem> getListComplainProduct() {
        return listComplainProduct;
    }

    public void setListComplainProduct(List<ListProductItem> listComplainProduct) {
        this.listComplainProduct = listComplainProduct;
    }

}
