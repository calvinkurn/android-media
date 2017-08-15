package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hendry on 8/15/2017.
 */

public class ProductVariantByPrdModel {
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("variant_option")
    @Expose
    private List<VariantOption> variantOption = null;
    @SerializedName("variant_data")
    @Expose
    private List<VariantDatum> variantData = null;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public List<VariantOption> getVariantOption() {
        return variantOption;
    }

    public void setVariantOption(List<VariantOption> variantOption) {
        this.variantOption = variantOption;
    }

    public List<VariantDatum> getVariantData() {
        return variantData;
    }

    public void setVariantData(List<VariantDatum> variantData) {
        this.variantData = variantData;
    }
}
