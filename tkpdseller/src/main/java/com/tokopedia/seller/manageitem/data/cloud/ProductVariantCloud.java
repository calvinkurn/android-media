package com.tokopedia.seller.manageitem.data.cloud;


import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.seller.manageitem.data.cloud.api.TomeProductApi;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByCatModel;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByPrdModel;

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
