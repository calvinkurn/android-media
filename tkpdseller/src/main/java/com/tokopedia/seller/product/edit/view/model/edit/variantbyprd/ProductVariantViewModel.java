
package com.tokopedia.seller.product.edit.view.model.edit.variantbyprd;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantoption.ProductVariantOptionParent;

public class ProductVariantViewModel {
    @SerializedName("parent_id")
    @Expose
    private long parentId;

    @SerializedName("default_child")
    @Expose
    private long defaultChild;

    @SerializedName("variant")
    @Expose
    private List<ProductVariantOptionParent> variantOptionParent = null;
    @SerializedName("product_variant")
    @Expose
    private List<ProductVariantCombinationViewModel> productVariant = null;

    public List<ProductVariantOptionParent> getVariantOptionParent() {
        return variantOptionParent;
    }

    public void setVariantOptionParent(List<ProductVariantOptionParent> variant) {
        this.variantOptionParent = variant;
    }

    public List<ProductVariantCombinationViewModel> getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(List<ProductVariantCombinationViewModel> productVariant) {
        this.productVariant = productVariant;
    }

}
