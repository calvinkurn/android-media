package com.tokopedia.ride.base.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/16/17.
 */

public class ProductResponseEntity {
    @SerializedName("products")
    @Expose
    List<ProductEntity> mProductEntities;

    public ProductResponseEntity() {
    }

    public List<ProductEntity> getProductEntities() {
        return mProductEntities;
    }
}
