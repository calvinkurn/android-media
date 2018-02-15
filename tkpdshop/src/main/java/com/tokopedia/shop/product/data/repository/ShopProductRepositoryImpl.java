package com.tokopedia.shop.product.data.repository;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.domain.repository.ShopProductRepository;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopProductRepositoryImpl implements ShopProductRepository {

    private final ShopProductCloudDataSource shopProductCloudDataSource;

    @Inject
    public ShopProductRepositoryImpl(ShopProductCloudDataSource shopProductCloudDataSource) {
        this.shopProductCloudDataSource = shopProductCloudDataSource;
    }

    @Override
    public Observable<ShopProductList> getShopProductList(ShopProductRequestModel shopProductRequestModel) {
        return shopProductCloudDataSource.getShopProductList(shopProductRequestModel).flatMap(new Func1<Response<DataResponse<ShopProductList>>, Observable<ShopProductList>>() {
            @Override
            public Observable<ShopProductList> call(Response<DataResponse<ShopProductList>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }
}
