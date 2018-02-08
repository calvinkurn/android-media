package com.tokopedia.shop.note.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteList;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopNoteCloudDataSource {

    private ShopApi shopApi;

    @Inject
    public ShopNoteCloudDataSource(ShopApi shopApi) {
        this.shopApi = shopApi;
    }

    public Observable<Response<DataResponse<ShopNoteList>>> getShopNoteList(String shopId) {
        return shopApi.getShopNotes(shopId);
    }

    public Observable<Response<DataResponse<ShopNoteDetail>>> getShopNoteDetail(String shopNoteId){
        return shopApi.getShopNoteDetail(shopNoteId);
    }
}
