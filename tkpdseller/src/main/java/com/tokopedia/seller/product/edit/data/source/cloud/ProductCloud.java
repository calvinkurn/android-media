package com.tokopedia.seller.product.edit.data.source.cloud;

import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ProductApi;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.lang.reflect.Type;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class ProductCloud {
    private final ProductApi productApi;

    public static final int SHOW_VARIANT = 1;

    @Inject
    public ProductCloud(ProductApi productApi) {
        this.productApi = productApi;
    }

    public Observable<ProductUploadResultModel> addProductSubmit(ProductViewModel productViewModel) {
        return productApi.addProductSubmit(productViewModel)
                .map(new DataResponseMapper<ProductUploadResultModel>());
    }

    public Observable<ProductUploadResultModel> editProduct(ProductViewModel productViewModel) {
        return productApi.editProductSubmit(productViewModel, String.valueOf(productViewModel.getProductId()))
                .map(new DataResponseMapper<ProductUploadResultModel>());
    }

    public Observable<ProductViewModel> getProductDetail(String productId) {
        return productApi.getProductDetail(productId, SHOW_VARIANT)
                .map(new DataResponseMapper<ProductViewModel>());
    }
}
