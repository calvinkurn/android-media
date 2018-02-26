
package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
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

    public List<ProductVariantOptionChild> getProductVariantOptionChild(int index) {
        if (variantOptionParent != null && index < variantOptionParent.size()
                && variantOptionParent.get(index).hasProductVariantOptionChild()) {
            return variantOptionParent.get(index).getProductVariantOptionChild();
        }
        return null;
    }

    public ProductVariantOptionParent getVariantOptionParent(int position) {
        int index = position - 1;
        return (variantOptionParent == null || index >= variantOptionParent.size()) ?
                null :
                variantOptionParent.get(index);
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

    public void removeSelectedVariantForLv1Value(String lv1Value) {
        if (productVariant == null || productVariant.size() == 0) {
            return;
        }
        for (int i = productVariant.size() - 1; i >= 0; i--) {
            if (productVariant.get(i).getLevel1String().equalsIgnoreCase(lv1Value)) {
                productVariant.remove(i);
            }
        }
    }

    public void removeSelectedVariantForLv1Value(String lv1Value, String lvl2Value) {
        if (productVariant == null || productVariant.size() == 0) {
            return;
        }
        for (int i = productVariant.size() - 1; i >= 0; i--) {
            if (productVariant.get(i).getLevel1String().equalsIgnoreCase(lv1Value) &&
                    productVariant.get(i).getLevel2String().equalsIgnoreCase(lvl2Value)) {
                productVariant.remove(i);
                break;
            }
        }
    }

    public void replaceSelectedVariantFor(String lv1Value,
                                          List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList) {
        removeSelectedVariantForLv1Value(lv1Value);
        productVariant.addAll(productVariantCombinationViewModelList);
    }

    public void replaceSelectedVariantFor(ProductVariantCombinationViewModel productVariantCombinationViewModel) {
        String lvl1String = productVariantCombinationViewModel.getLevel1String();
        String lvl2String = productVariantCombinationViewModel.getLevel2String();
        removeSelectedVariantForLv1Value(lvl1String, lvl2String);
        productVariant.add(productVariantCombinationViewModel);
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
