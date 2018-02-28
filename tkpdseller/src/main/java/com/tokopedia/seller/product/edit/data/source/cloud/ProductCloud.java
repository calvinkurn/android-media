package com.tokopedia.seller.product.edit.data.source.cloud;

import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeProductApi;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;

import java.util.ArrayList;
import java.util.Collection;
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

    @Inject
    public ProductCloud(TomeProductApi tomeProductApi) {
        this.tomeProductApi = tomeProductApi;
    }

    //TODO no need to have mapper
    public Observable<ProductUploadResultModel> addProductSubmit(String productViewModel) {
        return tomeProductApi.addProductSubmit(productViewModel)
                .map(new DataResponseMapper<ProductUploadResultModel>());
    }

    //TODO no need to have mapper
    public Observable<ProductUploadResultModel> editProduct(String productId, String productViewModel) {
        return tomeProductApi.editProductSubmit(productId, productViewModel)
                .map(new DataResponseMapper<ProductUploadResultModel>());
    }

    public Observable<ProductViewModel> getProductDetail(String productId) {
        return tomeProductApi.getProductDetail(productId, SHOW_VARIANT)
                .map(new DataResponseMapper<ProductViewModel>())
                .map(new Func1<ProductViewModel, ProductViewModel>() {
                    @Override
                    public ProductViewModel call(ProductViewModel productViewModel) {
                        ProductVariantViewModel productVariantViewModel = productViewModel.getProductVariant();
                        if (productVariantViewModel != null) {
                            List<ProductVariantOptionParent> productVariantOptionParentList =
                                    productVariantViewModel.getVariantOptionParent();
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
