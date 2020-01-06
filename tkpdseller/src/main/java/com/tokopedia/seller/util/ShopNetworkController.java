package com.tokopedia.seller.util;

import android.content.Context;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.BaseNetworkController;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.shop.ShopService;

/**
 * Created by normansyahputa on 3/20/17.
 */

@Deprecated
public class ShopNetworkController extends BaseNetworkController {
    public static final String SHOP_ID = "shop_id";

    public ShopNetworkController(Context context, ShopService shopService, Gson gson) {
        super(context, gson);
    }

    public static class ShopInfoParam {
        public String shopId;
        public String shopDomain;
    }

    public static class RequestParamFactory {
        public static final String KEY_SHOP_ID = "shop_id";
        public static final String KEY_USER_ID = "user_id";
        public static final String KEY_DEVICE_ID = "device_id";
        public static final String KEY_SHOP_INFO_PARAM = "shop_info_param";

        public static RequestParams generateRequestParam(String userid,
                                                         String deviceId,
                                                         ShopInfoParam shopInfoParam) {
            RequestParams requestParams = RequestParams.create();
            requestParams.putString(KEY_USER_ID, userid);
            requestParams.putString(KEY_DEVICE_ID, deviceId);
            requestParams.putObject(KEY_SHOP_INFO_PARAM, shopInfoParam);
            return requestParams;
        }
    }
}
