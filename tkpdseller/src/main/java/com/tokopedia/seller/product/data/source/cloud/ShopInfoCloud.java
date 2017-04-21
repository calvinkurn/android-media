package com.tokopedia.seller.product.data.source.cloud;

import android.text.TextUtils;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.product.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.topads.data.model.response.DataResponse;

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

    public static final String SHOP_ID = "shop_id";
    public static final String SHOW_ALL = "show_all";
    public static final String SHOP_DOMAIN = "shop_domain";

    @Inject
    public ShopInfoCloud(ShopApi api) {
        this.api = api;
    }

    public Observable<Response<DataResponse<ShopModel>>> getShopInfo(String userId, String deviceId,
                                                                     String shopId, String shopDomain) {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(shopId)) {
            params.put(SHOP_ID, shopId);
        }
        if (!TextUtils.isEmpty(shopDomain)) {
            params.put(SHOP_DOMAIN, shopDomain);
        }
        params.put(SHOW_ALL, "1");
        params = AuthUtil.generateParams(userId, deviceId, params);
        return api.getShopInfo(params);
    }

}
