package com.tokopedia.seller.product.edit.data.source.cloud;

import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeProductApi;

import javax.inject.Inject;

import rx.Observable;

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
    public Observable<ProductUploadResultModel> addProductSubmit(ProductViewModel productViewModel) {
        return tomeProductApi.addProductSubmit(productViewModel)
                .map(new DataResponseMapper<ProductUploadResultModel>());
    }

    //TODO no need to have mapper
    public Observable<ProductUploadResultModel> editProduct(ProductViewModel productViewModel) {
        return tomeProductApi.editProductSubmit(productViewModel, String.valueOf(productViewModel.getProductId()))
                .map(new DataResponseMapper<ProductUploadResultModel>());
    }

    public Observable<ProductViewModel> getProductDetail(String productId) {
        return tomeProductApi.getProductDetail(productId, SHOW_VARIANT)
                .map(new DataResponseMapper<ProductViewModel>());
    }
}
