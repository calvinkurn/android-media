package com.tokopedia.seller.product.variant.data.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeProductApi;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprdold.ProductVariantByPrdModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by hendry on 5/18/17.
 */

public class ProductVariantCloud {

    private TomeProductApi tomeProductApi;

    @Inject
    public ProductVariantCloud(TomeProductApi tomeProductApi) {
        this.tomeProductApi = tomeProductApi;
    }

    public Observable<Response<DataResponse<List<ProductVariantByCatModel>>>> fetchProductVariantByCat(long categoryId) {
        return tomeProductApi.getProductVariantByCat(categoryId);
    }

    public Observable<Response<DataResponse<ProductVariantByPrdModel>>> fetchProductVariantByPrd(long productId) {
        return tomeProductApi.getProductVariantByPrd(productId);
    }

}
