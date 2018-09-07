package com.tokopedia.transaction.addtocart.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.apiservices.logistics.LogisticsAuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.addtocart.model.kero.LogisticsData;
import com.tokopedia.transaction.addtocart.utils.KeroppiParam;

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

    private final LogisticsAuthService logisticsAuthService;
    private CompositeSubscription compositeSubscription;

    public KeroNetInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        logisticsAuthService = new LogisticsAuthService();
    }

    @Override
    public void calculateShipping(@NonNull Context context,
                                  @NonNull TKPDMapParam<String, String> params,
                                  @NonNull final CalculationListener listener) {

        Observable<Response<String>> observable = logisticsAuthService.getApi()
                .getLogisticsData(getRequestPayload(context, AuthUtil.generateParamsNetwork(context, params)));


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
                if (stringResponse.isSuccessful()) {
                    LogisticsData logisticsData = new Gson().fromJson(stringResponse.body(), LogisticsData.class);
                    if (logisticsData.getOngkirData() != null &&
                            logisticsData.getOngkirData().getOngkir() != null &&
                            logisticsData.getOngkirData().getOngkir().getData() != null) {
                        listener.onSuccess(logisticsData.getOngkirData().getOngkir().getData());
                    }
                }
            }
        };

        compositeSubscription.add(observable
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void calculateKeroCartAddressShipping(
            @NonNull final Context context, @NonNull final TKPDMapParam<String, String> params,
            @NonNull final OnCalculateKeroAddressShipping listener) {


        Observable<Response<String>> observable = logisticsAuthService.getApi()
                .getLogisticsData(getRequestPayload(context, AuthUtil.generateParamsNetwork(context, params)));

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
                    LogisticsData logisticsData = new Gson().fromJson(response.body(), LogisticsData.class);
                    if (logisticsData.getLogisticsError() != null && !logisticsData.getLogisticsError().get(0).getMessage().isEmpty()) {
                        listener.onFailure();
                    } else {
                        listener.onSuccess(logisticsData.getOngkirData().getOngkir().getData().getAttributes());
                    }
                } else {
                    listener.onFailure();
                }
            }
        };
        compositeSubscription.add(observable
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


    private String getRequestPayload(Context context, TKPDMapParam<String, String> params) {

        return String.format(
                CommonUtils.loadRawString(context.getResources(), R.raw.logistics_get_courier_query),
                params.get(KeroppiParam.CAT_ID),
                params.get(KeroppiParam.DESTINATION),
                params.get(KeroppiParam.FROM),
                params.get(KeroppiParam.INSURANCE),
                params.get(KeroppiParam.NAMES),
                params.get(KeroppiParam.ORDER_VALUE),
                params.get(KeroppiParam.ORIGIN),
                params.get(KeroppiParam.PRODUCT_INSURANCE),
                params.get(KeroppiParam.TOKEN),
                params.get(KeroppiParam.UT),
                params.get(KeroppiParam.WEIGHT),
                params.get(KeroppiParam.PARAM_OS_TYPE));
    }

    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
    }

}
