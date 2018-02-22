
package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;

public class ProductVariantViewModel implements Parcelable {

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

    public boolean hasSelectedVariant() {
        return productVariant != null && productVariant.size() > 0;
    }

    public void setProductVariant(List<ProductVariantCombinationViewModel> productVariant) {
        this.productVariant = productVariant;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.variantOptionParent);
        dest.writeList(this.productVariant);
    }

    public ProductVariantViewModel() {
    }

    protected ProductVariantViewModel(Parcel in) {
        this.variantOptionParent = new ArrayList<ProductVariantOptionParent>();
        in.readList(this.variantOptionParent, ProductVariantOptionParent.class.getClassLoader());
        this.productVariant = new ArrayList<ProductVariantCombinationViewModel>();
        in.readList(this.productVariant, ProductVariantCombinationViewModel.class.getClassLoader());
    }

    public static final Creator<ProductVariantViewModel> CREATOR = new Creator<ProductVariantViewModel>() {
        @Override
        public ProductVariantViewModel createFromParcel(Parcel source) {
            return new ProductVariantViewModel(source);
        }

        @Override
        public ProductVariantViewModel[] newArray(int size) {
            return new ProductVariantViewModel[size];
        }
    };
}
