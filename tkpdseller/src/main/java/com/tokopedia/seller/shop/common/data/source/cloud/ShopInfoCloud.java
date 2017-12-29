package com.tokopedia.seller.shop.common.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.common.data.response.DataResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopInfoCloud {
    private final ShopApi api;
    private final Context context;

    public static final String SHOP_ID = "shop_id";
    public static final String SHOW_ALL = "show_all";

    @Inject
    public ShopInfoCloud(@ApplicationContext Context context,
                         ShopApi api) {
        this.api = api;
        this.context = context;
    }

    public Observable<Response<DataResponse<ShopModel>>> getShopInfo() {
        String userId = SessionHandler.getLoginID(context);
        String deviceId = GCMHandler.getRegistrationId(context);
        String shopId = SessionHandler.getShopID(context);

        Map<String, String> params = new HashMap<>();
        params.put(SHOP_ID, shopId);
        params.put(SHOW_ALL, "1");
        params = AuthUtil.generateParams(userId, deviceId, params);
        return api.getShopInfo(params);
    }

}
