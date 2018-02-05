package com.tokopedia.shop.info.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.shop.common.data.source.cloud.ShopInfoCloud;
import com.tokopedia.shop.info.data.source.cache.ShopInfoCacheDataSource;
import com.tokopedia.shop.info.data.source.cloud.ShopInfoCloudDataSource;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ShopInfoDataSource {
    private ShopInfoCloudDataSource shopInfoCloudDataSource;
    private ShopInfoCacheDataSource shopInfoCacheDataSource;

    @Inject
    public ShopInfoDataSource(ShopInfoCacheDataSource shopInfoCacheDataSource, ShopInfoCloudDataSource shopInfoCloudDataSource) {
        this.shopInfoCacheDataSource = shopInfoCacheDataSource;
        this.shopInfoCloudDataSource = shopInfoCloudDataSource;
    }

    public Observable<Boolean> saveShopId(String shopId) {
        return shopInfoCacheDataSource.saveShopId(shopId);
    }

    public Observable<String> getShopId() {
        return shopInfoCacheDataSource.getShopId();
    }

    public Observable<ShopInfo> getShopInfo() {
        return shopInfoCacheDataSource.getShopId().flatMap(new Func1<String, Observable<ShopInfo>>() {
            @Override
            public Observable<ShopInfo> call(String s) {
                return shopInfoCloudDataSource.getShopInfo(s).flatMap(new Func1<Response<DataResponse<ShopInfo>>, Observable<ShopInfo>>() {
                    @Override
                    public Observable<ShopInfo> call(Response<DataResponse<ShopInfo>> dataResponseResponse) {
                        return Observable.just(dataResponseResponse.body().getData());
                    }
                });
            }
        });
    }
}
