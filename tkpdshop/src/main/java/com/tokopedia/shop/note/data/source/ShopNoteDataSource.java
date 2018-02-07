package com.tokopedia.shop.note.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.note.data.source.cloud.ShopNoteCloudDataSource;
import com.tokopedia.shop.note.data.source.cloud.model.ResponseList;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ShopNoteDataSource {
    private ShopNoteCloudDataSource shopNoteCloudDataSource;

    @Inject
    public ShopNoteDataSource(ShopNoteCloudDataSource shopNoteCloudDataSource) {
        this.shopNoteCloudDataSource = shopNoteCloudDataSource;
    }

    public Observable<List<ShopNote>> getShopNoteList(String shopId) {
        return shopNoteCloudDataSource.getShopNoteList(shopId).flatMap(new Func1<Response<DataResponse<ResponseList<ShopNote>>>, Observable<List<ShopNote>>>() {
            @Override
            public Observable<List<ShopNote>> call(Response<DataResponse<ResponseList<ShopNote>>> dataResponse) {
                return Observable.just(dataResponse.body().getData().getList());
            }
        });
    }
}
