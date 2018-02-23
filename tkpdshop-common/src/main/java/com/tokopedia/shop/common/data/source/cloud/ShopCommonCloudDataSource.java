package com.tokopedia.shop.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant;
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopCommonCloudDataSource {

    private final ShopCommonApi shopApi;
    private final UserSession userSession;

    public ShopCommonCloudDataSource(ShopCommonApi shopApi, UserSession userSession) {
        this.shopApi = shopApi;
        this.userSession = userSession;
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfo(String shopId) {
        String userId = userSession.getUserId();
        String osType = ShopCommonParamApiConstant.OS_TYPE_VALUE;
        String deviceId = userSession.getDeviceId();
        return shopApi.getShopInfo(shopId, userId, osType, deviceId);
    }

    public Observable<Response<DataResponse<ShopInfo>>> getShopInfoByDomain(String shopDomain) {
        String userId = userSession.getUserId();
        String osType = ShopCommonParamApiConstant.OS_TYPE_VALUE;
        String deviceId = userSession.getDeviceId();
        return shopApi.getShopInfoByDomain(shopDomain, userId, osType, deviceId);
    }
}
