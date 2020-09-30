package com.tokopedia.seller.manageitem.data.cloud;


import com.tokopedia.network.mapper.DataResponseMapper;
import com.tokopedia.seller.manageitem.data.cloud.api.TomeProductApi;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantOptionParent;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantViewModel;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductCloud {
    private final TomeProductApi tomeProductApi;

    public static final int SHOW_VARIANT = 1;
    public static final boolean IS_REAL_STOCK = true;

    @Inject
    public ProductCloud(TomeProductApi tomeProductApi) {
        this.tomeProductApi = tomeProductApi;
    }

    public Observable<Integer> addProductSubmit(String productViewModel) {
        return tomeProductApi.addProductSubmit(productViewModel)
                .map(dataResponse -> dataResponse.body().getData().getProductId());
    }

    public Observable<Integer> editProduct(String productId, String productViewModel) {
        return tomeProductApi.editProductSubmit(productId, productViewModel)
                .map(dataResponse -> dataResponse.body().getData().getProductId());
    }

    public Observable<ProductViewModel> getProductDetail(String productId) {
        return tomeProductApi.getProductDetail(productId, SHOW_VARIANT,IS_REAL_STOCK)
                .map(new DataResponseMapper<ProductViewModel>())
                .map(new Func1<ProductViewModel, ProductViewModel>() {
                    @Override
                    public ProductViewModel call(ProductViewModel productViewModel) {
                        ProductVariantViewModel productVariantViewModel = productViewModel.getProductVariant();
                        if (productVariantViewModel != null) {
                            List<ProductVariantOptionParent> productVariantOptionParentList = productVariantViewModel.getVariantOptionParent();
                            Collections.sort(productVariantOptionParentList, new Comparator<ProductVariantOptionParent>() {
                                @Override
                                public int compare(ProductVariantOptionParent o1, ProductVariantOptionParent o2) {
                                    return o1.getPosition() - o2.getPosition();
                                }
                            });
                        }
                        return productViewModel;
                    }
                });
    }
}
