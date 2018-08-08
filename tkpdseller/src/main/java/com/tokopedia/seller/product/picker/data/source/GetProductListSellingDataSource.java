package com.tokopedia.seller.product.picker.data.source;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by zulfikarrahman on 9/4/17.
 */

public class GetProductListSellingDataSource {

    private final GetProductListSellingDataSourceCloud getProductListSellingDataSourceCloud;

    @Inject
    public GetProductListSellingDataSource(GetProductListSellingDataSourceCloud getProductListSellingDataSourceCloud) {
        this.getProductListSellingDataSourceCloud = getProductListSellingDataSourceCloud;
    }

    public Observable<Response<ProductListSellerModel>> getProductListSeller(TKPDMapParam<String, String> parameters) {
        return getProductListSellingDataSourceCloud.getProductListSeller(parameters);
    }
}
