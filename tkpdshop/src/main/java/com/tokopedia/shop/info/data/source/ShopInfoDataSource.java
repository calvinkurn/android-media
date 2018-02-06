package com.tokopedia.shop.info.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.data.source.cloud.ShopInfoCloudDataSource;
import com.tokopedia.shop.info.data.source.cloud.model.ResponseList;
import com.tokopedia.shop.info.data.source.cloud.model.ShopNote;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ShopInfoDataSource {
    private ShopInfoCloudDataSource shopInfoCloudDataSource;

    @Inject
    public ShopInfoDataSource(ShopInfoCloudDataSource shopInfoCloudDataSource) {
        this.shopInfoCloudDataSource = shopInfoCloudDataSource;
    }

    public Observable<ShopInfo> getShopInfo(String shopId) {
        return shopInfoCloudDataSource.getShopInfo(shopId).flatMap(new Func1<Response<DataResponse<ShopInfo>>, Observable<ShopInfo>>() {
            @Override
            public Observable<ShopInfo> call(Response<DataResponse<ShopInfo>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }

    public Observable<List<ShopNote>> getShopNoteList(String shopId) {
        return shopInfoCloudDataSource.getShopNoteList(shopId).flatMap(new Func1<Response<DataResponse<ResponseList<ShopNote>>>, Observable<List<ShopNote>>>() {
            @Override
            public Observable<List<ShopNote>> call(Response<DataResponse<ResponseList<ShopNote>>> dataResponse) {
                return Observable.just(dataResponse.body().getData().getList());
            }
        });
    }
}
