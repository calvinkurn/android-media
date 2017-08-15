package com.tokopedia.seller.product.variant.data.cloud;

import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeApi;
import com.tokopedia.seller.product.variant.data.model.ProductVariantModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by hendry on 5/18/17.
 */

public class ProductVariantCloud {

    private TomeApi tomeApi;

    @Inject
    public ProductVariantCloud(TomeApi tomeApi) {
        this.tomeApi = tomeApi;
    }

    public Observable<Response<DataResponse<List<ProductVariantModel>>>> fetchProductVariant(long categoryId) {
        return tomeApi.getProductVariant(categoryId);
    }

}
