package com.tokopedia.shop.info.data.source.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.AuthUtil;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.info.data.source.cloud.api.ShopApi;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * @author hendry on 4/4/17.
 */

public class ShopInfoCacheDataSource {

    private static final String SHOP_SESSION = "SHOP_SESSION";
    private static final String DEFAULT_EMPTY_SHOP_ID = "-1";
    public static final String SHOP_ID = "shop_id";

    private Context context;

    @Inject
    public ShopInfoCacheDataSource(Context context) {
        this.context = context;
    }

    public Observable<String> getShopId() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                SharedPreferences sharedPrefs = context.getSharedPreferences(SHOP_SESSION, Context.MODE_PRIVATE);
                String shopId = sharedPrefs.getString(SHOP_ID, DEFAULT_EMPTY_SHOP_ID);
                subscriber.onNext(shopId);
            }
        });
    }

    public Observable<Boolean> saveShopId(final String shopId) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                SharedPreferences sharedPrefs = context.getSharedPreferences(SHOP_SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(SHOP_ID, shopId);
                editor.apply();
                subscriber.onNext(true);
            }
        });
    }
}
