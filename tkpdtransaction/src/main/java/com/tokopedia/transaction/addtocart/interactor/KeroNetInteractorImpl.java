package com.tokopedia.transaction.addtocart.interactor;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.logistics.LogisticsAuthService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.addtocart.model.kero.LogisticsData;
import com.tokopedia.transaction.addtocart.utils.KeroppiParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
                .getLogisticsData(getRequestPayload(context, params));


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
                    listener.onSuccess(logisticsData.getOngkirData().getOngkir().getData());

                } else {
                    throw new RuntimeException("Empty Response");
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
                .getLogisticsData(getRequestPayload(context, params));

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
                    listener.onSuccess(logisticsData.getOngkirData().getOngkir().getData().getAttributes());
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
                loadRawString(context.getResources(), R.raw.logistics_get_courier_query),
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
                params.get(KeroppiParam.WEIGHT));
    }

    private String loadRawString(Resources resources, int resId) {
        InputStream rawResource = resources.openRawResource(resId);
        String content = streamToString(rawResource);
        try {
            rawResource.close();
        } catch (IOException e) {
        }
        return content;
    }

    private String streamToString(InputStream in) {
        String temp;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp + "\n");
            }
        } catch (IOException e) {
        }
        return stringBuilder.toString();
    }

    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
    }

}
