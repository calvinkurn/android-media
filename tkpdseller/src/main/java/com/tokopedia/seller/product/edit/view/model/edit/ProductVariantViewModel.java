
package com.tokopedia.seller.product.edit.view.model.edit;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariantViewModel {

    @SerializedName("variant")
    @Expose
    private List<ProductVariantViewModel> variant = null;
    @SerializedName("product_variant")
    @Expose
    private List<ProductVariantCombinationViewModel> productVariant = null;

    public List<ProductVariantViewModel> getVariant() {
        return variant;
    }

    public void setVariant(List<ProductVariantViewModel> variant) {
        this.variant = variant;
    }

    public List<ProductVariantCombinationViewModel> getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(List<ProductVariantCombinationViewModel> productVariant) {
        this.productVariant = productVariant;
    }

}
