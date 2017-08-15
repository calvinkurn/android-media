package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 8/15/2017.
 */

public class ProductVariantSubmit {
    @SerializedName("variant_data")
    @Expose
    private VariantData variantData;

    /**
     * consist of the user selection of
     * 1. Summary of variant and
     * 2. Detail of the combination for variant level 1+ variant level 2 + so on
     * @return variant data
     */
    public VariantData getVariantData() {
        return variantData;
    }

    /**
     * consist of the user selection of
     * 1. Summary of variant and
     * 2. Detail of the combination for variant level 1+ variant level 2 + so on
     * @param variantData
     */
    public void setVariantData(VariantData variantData) {
        this.variantData = variantData;
    }

}
