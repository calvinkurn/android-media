package com.tokopedia.shop.info.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopInfoCloudDataSource {

    private ShopApi shopApi;

    @Inject
    public ShopInfoCloudDataSource(ShopApi shopApi) {
        this.shopApi = shopApi;
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfo(String shopId) {
        return shopApi.getShopInfo(shopId);
    }
}
