package com.tokopedia.seller.product.variant.view.model;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseIntArray;

import com.tokopedia.seller.base.view.adapter.ItemType;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/20/17.
 */
public class ProductVariantDashboardViewModel implements ItemType, Parcelable {

    public final static int TYPE = 123;

    // example "Merah"
    private ProductVariantOptionChild productVariantOptionChildLv1;

    // example [XXL: stock 1 price 1000] - [L:stock 2 price 2000]
    // if level 2 is null, this will belong to level 1 [Merah: stock 1 price 1000]
    private List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList = new ArrayList<>();

    public ProductVariantDashboardViewModel(ProductVariantOptionChild productVariantOptionChild) {
        this.productVariantOptionChildLv1 = productVariantOptionChild;
    }

    public void addCombinationModelIfAligned( @NonNull ProductVariantCombinationViewModel productVariantCombinationViewModel,
                                             @Nullable List<ProductVariantOptionChild> productVariantOptionChildLv2LookUp,
                                             SparseIntArray mapPvoToIndex) {
        List<Integer> optionIntegerList = productVariantCombinationViewModel.getOpt();
        if (optionIntegerList != null && optionIntegerList.size() != 0) {
            int level1IdOrPvo = productVariantOptionChildLv1.gettId() > 0 ? productVariantOptionChildLv1.gettId() :
                    productVariantOptionChildLv1.getPvo();

            // to ignore invalid data from server
            if (productVariantCombinationViewModel.getOpt() == null || productVariantCombinationViewModel.getOpt().size() == 0) {
                return;
            }
            if (productVariantOptionChildLv2LookUp!= null && productVariantOptionChildLv2LookUp.size() > 0) { // has variant level 2
                if (productVariantCombinationViewModel.getOpt().size() < 2) {
                    // but the model is less than 2
                    return;
                }
            } else { // no variant level 2
                if (productVariantCombinationViewModel.getOpt().size() >= 2) {
                    // but the model has 2 option
                    return;
                }
            }

            if (level1IdOrPvo == productVariantCombinationViewModel.getOpt().get(0)) {

                // add the string name to the model
                // example: from "opt": [23495,23497] to [Level 1 = "Merah"; Level 2 = "XL"]
                productVariantCombinationViewModel.setLevel1String(productVariantOptionChildLv1.getValue());
                // lookup level 2 to get the string name
                if (productVariantOptionChildLv2LookUp != null && productVariantCombinationViewModel.getOpt().size() >= 2) {
                    int index = mapPvoToIndex.get(productVariantCombinationViewModel.getOpt().get(1));
                    productVariantCombinationViewModel.setLevel2String(productVariantOptionChildLv2LookUp.get(index).getValue());
                }
                this.productVariantCombinationViewModelList.add(productVariantCombinationViewModel);
            }
        } else { // do not have the List of integer, so we use the string value instead.
            if (productVariantCombinationViewModel.getLevel1String().equalsIgnoreCase(productVariantOptionChildLv1.getValue())) {
                // with exception the option is not correct, do not add it.
                if (productVariantOptionChildLv2LookUp != null && productVariantOptionChildLv2LookUp.size() > 0) { //has option level 2
                    if (TextUtils.isEmpty(productVariantCombinationViewModel.getLevel2String())) { // but no level 2 in model
                        return;
                    }
                } else { // has no option level 2
                    if (!TextUtils.isEmpty(productVariantCombinationViewModel.getLevel2String())) { // but has level 2 in model
                        return;
                    }
                }
                this.productVariantCombinationViewModelList.add(productVariantCombinationViewModel);
            }
        }
    }

    public void addCombinationModel(@Nullable List<ProductVariantOptionChild> productVariantOptionChildLv2LookUp) {
        if (productVariantOptionChildLv2LookUp == null || productVariantOptionChildLv2LookUp.size() == 0) {
            ProductVariantCombinationViewModel productVariantCombinationViewModel = new ProductVariantCombinationViewModel(
                    false,
                    0,
                    0,
                    "",
                    productVariantOptionChildLv1.getValue(),
                    ""
            );
            productVariantCombinationViewModelList.add(productVariantCombinationViewModel);
            return;
        }
        for (int j = 0, sizej = productVariantOptionChildLv2LookUp.size(); j < sizej; j++) {
            ProductVariantCombinationViewModel productVariantCombinationViewModel = new ProductVariantCombinationViewModel(
                    false,
                    0,
                    0,
                    "",
                    productVariantOptionChildLv1.getValue(),
                    productVariantOptionChildLv2LookUp.get(j).getValue()
            );
            productVariantCombinationViewModelList.add(productVariantCombinationViewModel);
        }
    }

    public boolean haslevel2() {
        if (productVariantCombinationViewModelList == null || productVariantCombinationViewModelList.size() == 0) {
            return false;
        }
        return !TextUtils.isEmpty(productVariantCombinationViewModelList.get(0).getLevel2String());
    }

    public boolean has1LevelOnly() {
        return (productVariantCombinationViewModelList != null && productVariantCombinationViewModelList.size() == 1 &&
                TextUtils.isEmpty(productVariantCombinationViewModelList.get(0).getLevel2String()));
    }

    public List<ProductVariantCombinationViewModel> getProductVariantCombinationViewModelList() {
        return productVariantCombinationViewModelList;
    }

    public ProductVariantOptionChild getProductVariantOptionChildLv1() {
        return productVariantOptionChildLv1;
    }

    public int removeSelectedVariantFor(String lv1Value, String lvl2Value) {
        if (productVariantCombinationViewModelList == null || productVariantCombinationViewModelList.size() == 0) {
            return -1;
        }
        for (int i = productVariantCombinationViewModelList.size() - 1; i >= 0; i--) {
            if (productVariantCombinationViewModelList.get(i).getLevel1String().equalsIgnoreCase(lv1Value) &&
                    productVariantCombinationViewModelList.get(i).getLevel2String().equalsIgnoreCase(lvl2Value)) {
                productVariantCombinationViewModelList.remove(i);
                return i;
            }
        }
        return -1;
    }

    public void replaceSelectedVariantFor(ProductVariantCombinationViewModel productVariantCombinationViewModel) {
        String lvl1String = productVariantCombinationViewModel.getLevel1String();
        String lvl2String = productVariantCombinationViewModel.getLevel2String();
        int removedIndex = removeSelectedVariantFor(lvl1String, lvl2String);
        if (removedIndex > -1) {
            productVariantCombinationViewModelList.add(removedIndex, productVariantCombinationViewModel);
        } else {
            productVariantCombinationViewModelList.add(productVariantCombinationViewModel);
        }
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

    protected ProductVariantDashboardViewModel(android.os.Parcel in) {
        this.productVariantOptionChildLv1 = in.readParcelable(ProductVariantOptionChild.class.getClassLoader());
        this.productVariantCombinationViewModelList = in.createTypedArrayList(ProductVariantCombinationViewModel.CREATOR);
    }

    public static final Creator<ProductVariantDashboardViewModel> CREATOR = new Creator<ProductVariantDashboardViewModel>() {
        @Override
        public ProductVariantDashboardViewModel createFromParcel(android.os.Parcel source) {
            return new ProductVariantDashboardViewModel(source);
        }

        @Override
        public ProductVariantDashboardViewModel[] newArray(int size) {
            return new ProductVariantDashboardViewModel[size];
        }
    };
}
