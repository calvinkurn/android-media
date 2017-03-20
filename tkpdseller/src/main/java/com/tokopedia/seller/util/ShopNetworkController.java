package com.tokopedia.seller.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.BaseNetworkController;
import com.tkpd.library.utils.network.CommonListener;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 3/20/17.
 */

public class ShopNetworkController extends BaseNetworkController {
    private ShopService shopService;

    public ShopNetworkController(Context context, ShopService shopService, Gson gson) {
        super(context, gson);
        this.shopService = shopService;
    }

    public void getShopInfo(String userid, String deviceId, ShopInfoParam shopInfoParam, final GetShopInfo getShopInfo) {
        getShopInfo(userid, deviceId, shopInfoParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getShopInfo.onError(e);
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                String stringData = response.body().getStringData();
                                Log.d("STUART", "getShopInfo : onNext : " + stringData);
                                ShopModel shopModel = gson.fromJson(stringData, ShopModel.class);
                                getShopInfo.onSuccess(shopModel);
                            } else {
                                throw new MessageErrorException(response.body().getErrorMessages().get(0));
                            }
                        } else {
                            BaseNetworkController.onResponseError(response.code(), getShopInfo);
                        }
                    }
                });
    }

    public Observable<ShopModel> getShopInfo2(String userid, String deviceId, ShopInfoParam shopInfoParam) {
        return getShopInfo(userid, deviceId, shopInfoParam).map(new Func1<Response<TkpdResponse>, ShopModel>() {
            @Override
            public ShopModel call(Response<TkpdResponse> response) {
                ShopModel shopModel = null;
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        String stringData = response.body().getStringData();
                        Log.d("STUART", "getShopInfo : onNext : " + stringData);
                        shopModel = gson.fromJson(stringData, ShopModel.class);
                    } else {
                        throw new MessageErrorException(response.body().getErrorMessages().get(0));
                    }
                } else {
                    onResponseError(response.code(), new CommonListener() {
                        @Override
                        public void onError(Throwable e) {
                            throw new RuntimeException(e);
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
                return shopModel;
            }
        });
    }

    public Observable<Response<TkpdResponse>> getShopInfo(String userid, String deviceId, ShopInfoParam shopInfoParam) {
        return shopService.getApi().getInfo(AuthUtil.generateParams(userid, deviceId, paramShopInfo(shopInfoParam)));
    }

    public Map<String, String> paramShopInfo(ShopInfoParam shopInfoParam) {
        Map<String, String> params = new TKPDMapParam<>();
        params.put("shop_id", shopInfoParam.shopId);
        if (shopInfoParam.shopDomain != null)
            params.put("shop_domain", shopInfoParam.shopDomain);
        params.put("show_all", "1");
        return params;
    }

    public interface GetShopInfo extends CommonListener {
        void onSuccess(ShopModel shopModel);
    }

    public static class ShopInfoParam {
        public String shopId;
        public String shopDomain;
        public int showAll;
    }

    public static class MessageErrorException extends RuntimeException {

        public MessageErrorException(String message) {
            super(message);
        }
    }

    public static class ManyRequestErrorException extends RuntimeException {

        public ManyRequestErrorException(String message) {
            super(message);
        }
    }
}
