package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hendry on 8/15/2017.
 */

public class VariantData {

    @SerializedName("variant")
    @Expose
    private List<Variant> variant = null;
    @SerializedName("product_variant")
    @Expose
    private List<ProductVariant> productVariant = null;

    /**
     * Summary of the selected variant list. for example. Color and size.
     * @return variant List of the selected colors (ex:"white", red") and sizes ("43", "44")
     */
    public List<Variant> getVariant() {
        return variant;
    }

    /**
     * Summary of the selected variant list. for example. Color and size.
     * @param variant List of the selected colors (ex:"white", red") and sizes ("43", "44")
     */
    public void setVariant(List<Variant> variant) {
        this.variant = variant;
    }

    /**
     * Detail of the combination for variant level 1+ variant level 2 + so on
     * @return  productVariant list of the metrics of selected variants
     */
    public List<ProductVariant> getProductVariant() {
        return productVariant;
    }

    /**
     * Detail of the combination for variant level 1+ variant level 2 + so on
     * @param productVariant list of the metrics of selected variants
     */
    public void setProductVariant(List<ProductVariant> productVariant) {
        this.productVariant = productVariant;
    }

}
