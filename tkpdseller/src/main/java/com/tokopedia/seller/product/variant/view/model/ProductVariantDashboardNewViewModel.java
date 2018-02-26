package com.tokopedia.seller.product.variant.view.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/20/17.
 */
public class ProductVariantDashboardNewViewModel implements ItemType, Parcelable {

    public final static int TYPE = 123;

    // example "Merah"
    private ProductVariantOptionChild productVariantOptionChildLv1;

    // example [XXL: stock 1 price 1000] - [L:stock 2 price 2000]
    // if level 2 is null, this will belong to level 1 [Merah: stock 1 price 1000]
    private List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList = new ArrayList<>();

    public ProductVariantDashboardNewViewModel(ProductVariantOptionChild productVariantOptionChild){
        this.productVariantOptionChildLv1 = productVariantOptionChild;
    }

    public void addCombinationModelIfAligned(@NonNull ProductVariantCombinationViewModel productVariantCombinationViewModel,
                                             @Nullable List<ProductVariantOptionChild> productVariantOptionChildLv2LookUp){
        List<Integer> optionIntegerList = productVariantCombinationViewModel.getOpt();
        if (optionIntegerList != null && optionIntegerList.size() != 0) {
            if (productVariantCombinationViewModel.getOpt().contains(productVariantOptionChildLv1.getPvo())){

                // add the string name to the model
                // example: from "opt": [23495,23497] to [Level 1 = "Merah"; Level 2 = "XL"]
                productVariantCombinationViewModel.setLevel1String(productVariantOptionChildLv1.getValue());
                // lookup level 2 to get the string name
                if (productVariantOptionChildLv2LookUp != null && productVariantCombinationViewModel.getOpt().size() >= 2) {
                    for (int i = 0, sizei = productVariantOptionChildLv2LookUp.size(); i < sizei; i++) {
                        ProductVariantOptionChild productVariantOptionChildLv2 = productVariantOptionChildLv2LookUp.get(i);
                        if (productVariantOptionChildLv2.getPvo() == productVariantCombinationViewModel.getOpt().get(1)) {
                            productVariantCombinationViewModel.setLevel2String(productVariantOptionChildLv2.getValue());
                        }
                    }
                }
                this.productVariantCombinationViewModelList.add(productVariantCombinationViewModel);
            }
        } else {
            //TODO check logic for this.
            if (productVariantCombinationViewModel.getLevel1String().equalsIgnoreCase(productVariantOptionChildLv1.getValue())) {
                this.productVariantCombinationViewModelList.add(productVariantCombinationViewModel);
            }
        }
    }

    public boolean haslevel2(){
        if (productVariantCombinationViewModelList == null || productVariantCombinationViewModelList.size() == 0) {
            return false;
        }
        return !TextUtils.isEmpty(productVariantCombinationViewModelList.get(0).getLevel2String());
    }

    public List<ProductVariantCombinationViewModel> getProductVariantCombinationViewModelList() {
        return productVariantCombinationViewModelList;
    }

    public ProductVariantOptionChild getProductVariantOptionChildLv1() {
        return productVariantOptionChildLv1;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(this.productVariantOptionChildLv1, flags);
        dest.writeTypedList(this.productVariantCombinationViewModelList);
    }

    protected ProductVariantDashboardNewViewModel(android.os.Parcel in) {
        this.productVariantOptionChildLv1 = in.readParcelable(ProductVariantOptionChild.class.getClassLoader());
        this.productVariantCombinationViewModelList = in.createTypedArrayList(ProductVariantCombinationViewModel.CREATOR);
    }

    public static final Creator<ProductVariantDashboardNewViewModel> CREATOR = new Creator<ProductVariantDashboardNewViewModel>() {
        @Override
        public ProductVariantDashboardNewViewModel createFromParcel(android.os.Parcel source) {
            return new ProductVariantDashboardNewViewModel(source);
        }

        @Override
        public ProductVariantDashboardNewViewModel[] newArray(int size) {
            return new ProductVariantDashboardNewViewModel[size];
        }
    };
}
