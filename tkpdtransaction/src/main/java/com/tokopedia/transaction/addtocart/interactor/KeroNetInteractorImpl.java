package com.tokopedia.transaction.addtocart.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.addtocart.model.kero.Rates;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Herdi_WORK on 20.09.16.
 */
public class KeroNetInteractorImpl implements KeroNetInteractor {

    private final KeroAuthService keroService;
    private CompositeSubscription compositeSubscription;

    public KeroNetInteractorImpl() {
        keroService = new KeroAuthService(0);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void calculateShipping(@NonNull Context context,
                                  @NonNull TKPDMapParam<String, String> params,
                                  @NonNull final CalculationListener listener) {
        Observable<Response<String>> observable = keroService
                .getApi()
                .calculateShippingRate(AuthUtil.generateParamsNetwork(context, params));

        Subscriber<Response<String>> subscriber = new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Response<String> stringResponse) {
                if (stringResponse.body() != null) {
                    Rates rates = new Gson().fromJson(stringResponse.body(), Rates.class);
                    listener.onSuccess(rates.getData());
                } else {
                    throw new RuntimeException("Empty Response");
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void calculateKeroCartAddressShipping(
            @NonNull final Context context, @NonNull final TKPDMapParam<String, String> param,
            @NonNull final OnCalculateKeroAddressShipping listener) {
        Observable<Response<String>> observable = keroService.getApi().calculateShippingRate(param);
        Subscriber<Response<String>> subscriber = new Subscriber<Response<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onFailure();
            }

            @Override
            public void onNext(Response<String> response) {
                if (response.isSuccessful()) {
                    Rates rates = new Gson().fromJson(response.body(), Rates.class);
                    listener.onSuccess(rates.getData().getAttributes());
                } else {
                    listener.onFailure();
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
    }
}
