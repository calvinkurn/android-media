package com.tokopedia.topads.sdk.network.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tokopedia.topads.sdk.base.GlobalConstant;
import com.tokopedia.topads.sdk.base.TKPDMapParam;
import com.tokopedia.topads.sdk.network.HttpMethod;
import com.tokopedia.topads.sdk.network.HttpRequest;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;

import java.io.IOException;

/**
 * @author madi on 4/27/17.
 */

public class AddFavoriteShopService extends IntentService {

    public static final String EXTRAS_SESSION_ID = "EXTRAS_SESSION_ID";
    public static final String EXTRAS_SHOP_ID = "EXTRAS_SHOP_ID";
    public static final String EXTRAS_PARAMS = "EXTRAS_PARAMS";
    public static final String EXTRAS_AD_KEY = "EXTRAS_AD_KEY";

    private static final String SERVICE_NAME = "AddFavoriteShopService";
    private static final String AD_KEY_VALUE = "fav_shop";

    private static final String TAG = "AddFavoriteShopService";

    public AddFavoriteShopService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {

            String sessionId
                    = intent.hasExtra(EXTRAS_SESSION_ID)
                    ? intent.getStringExtra(EXTRAS_SESSION_ID)
                    : "";
            String shopId
                    = intent.hasExtra(EXTRAS_SHOP_ID) ? intent.getStringExtra(EXTRAS_SHOP_ID) : "";
            String adKey
                    = intent.hasExtra(EXTRAS_AD_KEY) ? intent.getStringExtra(EXTRAS_AD_KEY) : "";

            TKPDMapParam<String, String> param = new TKPDMapParam<>();
            param.put(GlobalConstant.KEY_USER_ID, sessionId);
            param.put(GlobalConstant.KEY_SHOP_ID, shopId);
            param.put(GlobalConstant.KEY_AD_KEY, adKey);


            Log.d(TAG, "onHandleIntent: " + param.toString());
            postFavoriteShop(sessionId, param);
        }
    }

    private void postFavoriteShop(String sessionId, TKPDMapParam<String, String> params) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(GlobalConstant.BASE_WS4_DOMAIN + GlobalConstant.PATH_ADD_FAVORITE_SHOP)
                .addHeader(GlobalConstant.KEY_USER_ID, sessionId)
                .setMethod(HttpMethod.POST)
                .addParameters(params)
                .build();
        RawHttpRequestExecutor executor = RawHttpRequestExecutor.newInstance(httpRequest);
        try {
            String response = executor.makeRequest();
            Log.d("Service", "postFavoriteShop: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
