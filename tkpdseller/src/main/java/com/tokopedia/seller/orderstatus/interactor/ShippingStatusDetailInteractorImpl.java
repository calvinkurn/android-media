package com.tokopedia.seller.orderstatus.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.selling.model.modelEditRef.DataResponseEditRef;
import com.tokopedia.seller.selling.network.apiservices.MyShopOrderActService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/30/17. Tokopedia
 */

public class ShippingStatusDetailInteractorImpl implements ShippingStatusDetailInteractor{

    private static final String STUART = "STUART";
    private static final String FACADE_SHOP_TRANSACTION = "FACADE SHOP TRANSACTION";

    private CompositeSubscription compositeSubscription;

    private MyShopOrderActService myShopOrderActService;

    public ShippingStatusDetailInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        myShopOrderActService = new MyShopOrderActService();
    }

    @Override
    public void editRefNum(@NonNull Context context,
                           @NonNull final Map<String, String> params,
                           @NonNull final onEditRefNumListener listener) {
        compositeSubscription.add(myShopOrderActService
                .getApi()
                .editShippingRef(AuthUtil.generateParams(context, params))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                listener.onFailed("terjadi masalah koneksi");
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    DataResponseEditRef data = gson.fromJson(jsonObject.toString(), DataResponseEditRef.class);

                                    if (data.getIsSuccess() == 1) {
                                        listener.onSuccess(params.get("shipping_ref"));
                                    } else {
                                        listener.onFailed(response.getErrorMessages().get(0));
                                    }
                                } catch (JSONException je) {
                                    Log.e(STUART, FACADE_SHOP_TRANSACTION + je.getLocalizedMessage());
                                }
                            }
                        }
                ));
    }

    @Override
    public void retryCourierPickUp(@NonNull Context context,
                                   @NonNull TKPDMapParam<String, String> params,
                                   @NonNull final onRetryPickupListener listener) {
        compositeSubscription.add(myShopOrderActService.getApi()
                .retryPickUp(AuthUtil.generateParamsNetwork(context, params))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.noConnection();
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> tkpdResponse) {
                        if(!tkpdResponse.body().isError()) {
                            listener.onSuccess(tkpdResponse.body().getStatusMessages().get(0));
                        } else {
                            listener.onFailed(tkpdResponse.body().getErrorMessageJoined());
                        }
                    }
                }));
    }
    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
    }
}
