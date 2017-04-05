package com.tokopedia.seller.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.BaseNetworkController;
import com.tkpd.library.utils.network.CommonListener;
import com.tokopedia.core.base.domain.RequestParams;
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

import static com.tokopedia.seller.util.ShopNetworkController.RequestParamFactory.KEY_SHOP_ID;
import static com.tokopedia.seller.util.ShopNetworkController.RequestParamFactory.KEY_SHOP_INFO_PARAM;

/**
 * Created by normansyahputa on 3/20/17.
 */

public class ShopNetworkController extends BaseNetworkController {
    public static final String SHOP_ID = "shop_id";
    public static final String SHOW_ALL = "show_all";
    public static final String SHOP_DOMAIN = "shop_domain";
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

    public Observable<ShopModel> getShopInfo2(RequestParams requestParams) {
        String userid = requestParams.getString(KEY_SHOP_INFO_PARAM, "");
        String deviceId = requestParams.getString(KEY_SHOP_ID, "");
        ShopInfoParam shopInfoParam = (ShopInfoParam) requestParams.getObject(KEY_SHOP_INFO_PARAM);
        return getShopInfo(userid, deviceId, shopInfoParam).map(new Func1<Response<TkpdResponse>, ShopModel>() {
            @Override
            public ShopModel call(Response<TkpdResponse> response) {
                ShopModel shopModel = null;
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        String stringData = response.body().getStringData();
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

    public Observable<ShopModel> getShopInfo2(String userid, String deviceId, ShopInfoParam shopInfoParam) {
        return getShopInfo(userid, deviceId, shopInfoParam).map(new Func1<Response<TkpdResponse>, ShopModel>() {
            @Override
            public ShopModel call(Response<TkpdResponse> response) {
                ShopModel shopModel = null;
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        String stringData = response.body().getStringData();
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
        params.put(SHOP_ID, shopInfoParam.shopId);
        if (shopInfoParam.shopDomain != null)
            params.put(SHOP_DOMAIN, shopInfoParam.shopDomain);
        params.put(SHOW_ALL, "1");
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

    public static class RequestParamFactory {
        public static final String KEY_SHOP_ID = "shop_id";
        public static final String KEY_SHOP_DOMAIN = "shop_domain";
        public static final String KEY_SHOW_ALL = "show_all";
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
