package com.tokopedia.shop.product.data.repository;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.product.data.source.cloud.ShopFilterCloudDataSource;
import com.tokopedia.shop.product.data.source.cloud.ShopProductCloudDataSource;
import com.tokopedia.shop.product.data.source.cloud.model.DynamicFilterModel;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct;
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
    private ShopFilterCloudDataSource shopFilterCloudDataSource;

    @Inject
    public ShopProductRepositoryImpl(ShopProductCloudDataSource shopProductCloudDataSource,
                                     ShopFilterCloudDataSource shopFilterCloudDataSource) {
        this.shopProductCloudDataSource = shopProductCloudDataSource;
        this.shopFilterCloudDataSource = shopFilterCloudDataSource;
    }

    @Override
    public Observable<PagingList<ShopProduct>> getShopProductList(ShopProductRequestModel shopProductRequestModel) {
        return shopProductCloudDataSource.getShopProductList(shopProductRequestModel).flatMap(new Func1<Response<DataResponse<PagingList<ShopProduct>>>, Observable<PagingList<ShopProduct>>>() {
            @Override
            public Observable<PagingList<ShopProduct>> call(Response<DataResponse<PagingList<ShopProduct>>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }

    @Override
    public Observable<DynamicFilterModel.DataValue> getShopProductFilter() {
        return shopFilterCloudDataSource.getDynamicFilter().flatMap(new Func1<Response<DynamicFilterModel>, Observable<DynamicFilterModel.DataValue>>() {
            @Override
            public Observable<DynamicFilterModel.DataValue> call(Response<DynamicFilterModel> response) {
                return Observable.just(response.body().getData());
            }
        });
    }
}
