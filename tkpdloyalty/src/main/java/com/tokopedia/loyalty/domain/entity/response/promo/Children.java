package com.tokopedia.loyalty.domain.entity.response.promo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public class Children {

    @SerializedName("sub_category")
    @Expose
    private SubCategory subCategory;

    public SubCategory getSubCategory() {
        return subCategory;
    }
}
