
package com.tokopedia.seller.product.variant.data.model.variantbyprd;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.constant.StockTypeDef;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;

public class ProductVariantViewModel implements Parcelable {

    @SerializedName("variant")
    @Expose
    private List<ProductVariantOptionParent> variantOptionParent = new ArrayList<>();

    @SerializedName("product_variant")
    @Expose
    private List<ProductVariantCombinationViewModel> productVariant = new ArrayList<>();

    public List<ProductVariantOptionParent> getVariantOptionParent() {
        return variantOptionParent;
    }

    public void setVariantOptionParent(List<ProductVariantOptionParent> variantOptionParent) {
        this.variantOptionParent = variantOptionParent;
    }

    public List<ProductVariantOptionChild> getProductVariantOptionChild(int index) {
        if (variantOptionParent != null && index < variantOptionParent.size()
                && variantOptionParent.get(index).hasProductVariantOptionChild()) {
            return variantOptionParent.get(index).getProductVariantOptionChild();
        }
        return null;
    }

    public void replaceVariantOptionParentFor(int level, ProductVariantOptionParent productVariantOptionParent) {
        int index = level - 1;
        if (variantOptionParent == null) {
            variantOptionParent = new ArrayList<>();
        }
        if (variantOptionParent.size() > index) {
            variantOptionParent.remove(index);
        }
        if (productVariantOptionParent != null && productVariantOptionParent.hasProductVariantOptionChild()) {
            variantOptionParent.add(index, productVariantOptionParent);
        }
    }

    public void replaceVariantOptionChildFor(int level, ProductVariantOptionChild productVariantOptionChildToAdd) {
        int index = level - 1;
        if (variantOptionParent == null || variantOptionParent.size() < level) {
            return;
        }
        ProductVariantOptionParent productVariantOptionParent = variantOptionParent.get(index);
        if (productVariantOptionParent == null || !productVariantOptionParent.hasProductVariantOptionChild()) {
            return;
        }
        List<ProductVariantOptionChild> productVariantOptionChildList = productVariantOptionParent.getProductVariantOptionChild();
        for (int i = 0, sizei = productVariantOptionChildList.size(); i < sizei; i++) {
            ProductVariantOptionChild prevProductVariantOptionChild = productVariantOptionChildList.get(i);
            if ((productVariantOptionChildToAdd.getVuv()!= 0 &&
                    prevProductVariantOptionChild.getVuv() == productVariantOptionChildToAdd.getVuv()) ||
                    prevProductVariantOptionChild.getValue().equalsIgnoreCase(productVariantOptionChildToAdd.getValue())) {
                productVariantOptionChildList.remove(i);
                productVariantOptionChildList.add(i, productVariantOptionChildToAdd);
                break;
            }
        }
    }

    public ProductVariantOptionParent getVariantOptionParent(int index) {
        return (variantOptionParent == null || index >= variantOptionParent.size()) ?
                null :
                variantOptionParent.get(index);
    }

    public List<ProductVariantCombinationViewModel> getProductVariant() {
        return productVariant;
    }

    public boolean hasSelectedVariant() {
        return productVariant != null && productVariant.size() > 0;
    }

    public @StockTypeDef int getCalculateProductStatus() {
        boolean hasAnyAlwaysAvailable = false;
        for (ProductVariantCombinationViewModel productVariantCombinationViewModel : productVariant) {
            // once we get the limited, we assume the status is all limited.
            if (productVariantCombinationViewModel.isLimitedStock()) {
                return StockTypeDef.TYPE_ACTIVE_LIMITED;
            }
            // get "always available", we will continue loop. We want to differentiate between active or active limited.
            if (productVariantCombinationViewModel.alwaysAvailable()) {
                hasAnyAlwaysAvailable = true;
            }
        }
        if (hasAnyAlwaysAvailable) {
            return StockTypeDef.TYPE_ACTIVE;
        }
        // no any available or limited. so it is always empty.
        return StockTypeDef.TYPE_WAREHOUSE;
    }

    public void setProductVariant(List<ProductVariantCombinationViewModel> productVariant) {
        this.productVariant = productVariant;
    }

    private int removeSelectedVariantFor(String lv1Value) {
        if (productVariant == null || productVariant.size() == 0) {
            return -1;
        }
        int firstIndex = -1;
        for (int i = productVariant.size() - 1; i >= 0; i--) {
            if (productVariant.get(i).getLevel1String().equalsIgnoreCase(lv1Value)) {
                firstIndex = i;
                productVariant.remove(i);
            }
        }
        return firstIndex;
    }

    public void replaceSelectedVariantFor(String lv1Value,
                                          List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList) {
        int index = removeSelectedVariantFor(lv1Value);
        if (index > -1) {
            productVariant.addAll(index, productVariantCombinationViewModelList);
        } else {
            productVariant.addAll(productVariantCombinationViewModelList);
        }
    }

    private int removeSelectedVariantFor(String lv1Value, String lvl2Value) {
        if (productVariant == null || productVariant.size() == 0) {
            return -1;
        }
        for (int i = productVariant.size() - 1; i >= 0; i--) {
            if (productVariant.get(i).getLevel1String().equalsIgnoreCase(lv1Value) &&
                    productVariant.get(i).getLevel2String().equalsIgnoreCase(lvl2Value)) {
                productVariant.remove(i);
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
            productVariant.add(removedIndex, productVariantCombinationViewModel);
        } else {
            productVariant.add(productVariantCombinationViewModel);
        }
    }

    public void changeStockTo(@StockTypeDef int stockType) {
        if (productVariant != null) {
            @StockTypeDef int prevStockType = getCalculateProductStatus();
            for (ProductVariantCombinationViewModel productVariantCombinationViewModel : productVariant) {
                if (stockType == StockTypeDef.TYPE_WAREHOUSE) {
                    productVariantCombinationViewModel.setActive(false);
                    productVariantCombinationViewModel.setStock(0);
                } else {
                    if (stockType == StockTypeDef.TYPE_ACTIVE_LIMITED) {
                        // if previous stock type is TYPE_EMPTY, change to 1
                        if (prevStockType != StockTypeDef.TYPE_ACTIVE_LIMITED) {
                            productVariantCombinationViewModel.setStock(1);
                            productVariantCombinationViewModel.setActive(true);
                        }
                    } else {
                        productVariantCombinationViewModel.setStock(0);
                        productVariantCombinationViewModel.setActive(true);
                    }
                }
            }
        }
    }

    public void changePriceTo(double value){
        if (hasSelectedVariant()) {
            List<ProductVariantCombinationViewModel> combinationViewModelList = productVariant;
            for (ProductVariantCombinationViewModel combinationViewModel: combinationViewModelList) {
                combinationViewModel.setPriceVar(value);
            }
        }
    }

    public double getMinVariantProductPrice() {
        double minPrice = Double.MAX_VALUE;
        for (ProductVariantCombinationViewModel productVariantCombinationViewModel : productVariant) {
            double price = productVariantCombinationViewModel.getPriceVar();
            if (price < minPrice) {
                minPrice = price;
            }
        }
        if (minPrice == Double.MAX_VALUE) {
            return 0;
        }
        return minPrice;
    }


    /**
     * function to convert the pvo to tid and custom values to tid
     * if all are already converted to tid (means pvo == 0), the result should be same.
     * example pvo 100, 200, 300, 678  option [100,200] [100,300] [100,678]
     * will be converted to tid 1,2,3,4 option [1,2] [1,3][1,4]
     * the same goes for "Merah" "Kuning"; "S", "M" ,"L"  with option ["Merah", "S"] ["Kuning", "L"]
     * will be converted to 1,2; 3,4,5 with option [1,3] [2,5]
     */
    public ProductVariantViewModel generateTid() {
        if (!hasSelectedVariant()) {
            return this;
        }
        // get current selection for item level 1, level 2, and the matrix combination
        ProductVariantOptionParent productVariantOptionParentLevel1 = getVariantOptionParent(0);
        ProductVariantOptionParent productVariantOptionParentLevel2 = getVariantOptionParent(1);
        List<ProductVariantOptionChild> productVariantOptionChildLevel1List = productVariantOptionParentLevel1.getProductVariantOptionChild();
        List<ProductVariantOptionChild> productVariantOptionChildLevel2List = null;
        if (productVariantOptionParentLevel2 != null) {
            productVariantOptionChildLevel2List = productVariantOptionParentLevel2.getProductVariantOptionChild();
        }
        List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList = getProductVariant();

        // create the map for the lookup (this is for performance, instead we do loop each time to get the combination model)
        HashMap<String, Integer> mapLevel1 = new HashMap<>();
        HashMap<String, Integer> mapLevel2 = new HashMap<>();
        SparseIntArray mapPvoLevel1 = new SparseIntArray();
        SparseIntArray mapPvoLevel2 = new SparseIntArray();
        createOptionMap(productVariantOptionChildLevel1List, productVariantOptionChildLevel2List, mapLevel1, mapLevel2, mapPvoLevel1, mapPvoLevel2);

        if (mapPvoLevel1.size() == 0 && mapLevel1.size() == 0) {
            //means, it is already mapped
            return this;
        }

        // generate the matrix axb based on level 1 and level2.
        // example level1 has a variant, level 2 has b variants, the matrix will be (axb)
        // map is used to lookup if the value1x value2 already exist.
        for (int i = 0, sizei = productVariantCombinationViewModelList.size(); i < sizei; i++) {
            ProductVariantCombinationViewModel productVariantCombinationViewModel = productVariantCombinationViewModelList.get(i);
            String level1String = productVariantCombinationViewModel.getLevel1String();

            List<Integer> integerList = new ArrayList<>();
            Integer indexLevel1;
            if (TextUtils.isEmpty(level1String)) {
                // using pvo
                indexLevel1 = mapPvoLevel1.get(productVariantCombinationViewModel.getOpt().get(0));
            } else {
                indexLevel1 = mapLevel1.get(level1String);
            }
            int tIdLevel1 = productVariantOptionParentLevel1.getProductVariantOptionChild().get(indexLevel1).gettId();
            integerList.add(tIdLevel1);
            if (productVariantOptionParentLevel2 != null && productVariantOptionParentLevel2.hasProductVariantOptionChild()) {
                String level2String = productVariantCombinationViewModel.getLevel2String();
                Integer indexLevel2;
                if (TextUtils.isEmpty(level1String)) {
                    // using pvo
                    indexLevel2 = mapPvoLevel2.get(productVariantCombinationViewModel.getOpt().get(1));
                } else {
                    indexLevel2 = mapLevel2.get(level2String);
                }
                int tIdLevel2 = productVariantOptionParentLevel2.getProductVariantOptionChild().get(indexLevel2).gettId();
                integerList.add(tIdLevel2);
            }
            productVariantCombinationViewModel.setOpt(integerList);
        }
        return this;
    }

    private void createOptionMap(List<ProductVariantOptionChild> productVariantOptionChildLevel1List,
                                 List<ProductVariantOptionChild> productVariantOptionChildLevel2List,
                                 HashMap<String, Integer> mapLevel1, HashMap<String, Integer> mapLevel2,
                                 SparseIntArray mapPvoLevel1, SparseIntArray mapPvoLevel2) {
        int counter = 1;
        for (int i = 0, sizei = productVariantOptionChildLevel1List.size(); i < sizei; i++) {
            ProductVariantOptionChild productVariantOptionChild = productVariantOptionChildLevel1List.get(i);
            productVariantOptionChild.settId(counter++);
            // comes from custom value
            mapLevel1.put(productVariantOptionChild.getValue(), i);
            int pvo = productVariantOptionChild.getPvo();
            if (pvo == 0) { // means it has converted to tid
                mapPvoLevel1.put(productVariantOptionChild.gettId(), i);
            } else { // it comes from the server
                mapPvoLevel1.put(pvo, i);
            }
            productVariantOptionChild.setPvo(0);
        }

        if (productVariantOptionChildLevel2List != null) {
            for (int i = 0, sizei = productVariantOptionChildLevel2List.size(); i < sizei; i++) {
                ProductVariantOptionChild productVariantOptionChild = productVariantOptionChildLevel2List.get(i);
                productVariantOptionChild.settId(counter++);
                // comes from custom value
                mapLevel2.put(productVariantOptionChild.getValue(), i);
                int pvo = productVariantOptionChild.getPvo();
                if (pvo == 0) { // means it has converted to tid, comes from draft
                    mapPvoLevel2.put(productVariantOptionChild.gettId(), i);
                } else { // comes from server
                    mapPvoLevel2.put(pvo, i);
                }
                mapPvoLevel2.put(pvo, i);
                productVariantOptionChild.setPvo(0);
            }
        }
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
