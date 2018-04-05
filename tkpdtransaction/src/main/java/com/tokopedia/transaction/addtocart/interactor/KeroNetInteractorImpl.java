package com.tokopedia.transaction.addtocart.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.addtocart.model.kero.Data;
import com.tokopedia.transaction.addtocart.utils.KeroppiParam;
import com.tokopedia.transaction.graphql.logistics.LogisticsRateQuery;
import com.tokopedia.transaction.graphql.logistics.OngkirRatesInput;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Herdi_WORK on 20.09.16.
 */
public class KeroNetInteractorImpl implements KeroNetInteractor {

    //    private final KeroAuthService keroService;
    private CompositeSubscription compositeSubscription;
    private ApolloClient apolloClient;

    public KeroNetInteractorImpl() {
//        keroService = new KeroAuthService(0);
        compositeSubscription = new CompositeSubscription();

        apolloClient = ApolloClient.builder()
                .okHttpClient(OkHttpFactory.create().buildClientDefaultAuth())
                .serverUrl(TkpdBaseURL.HOME_DATA_BASE_URL)
                .build();
    }

    @Override
    public void calculateShipping(@NonNull Context context,
                                  @NonNull TKPDMapParam<String, String> params,
                                  @NonNull final CalculationListener listener) {

        ApolloWatcher<LogisticsRateQuery.Data> apolloWatcher = apolloClient.newCall(LogisticsRateQuery.builder()
                .input(getOngkirRatesInputFromParam(params))
                .build()
        ).watcher();

        Subscriber<LogisticsRateQuery.Data> subscriber = new Subscriber<LogisticsRateQuery.Data>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(LogisticsRateQuery.Data stringResponse) {
                if (stringResponse.ongkir().rates() != null) {
                    Data data = new Gson().fromJson(CacheUtil.convertModelToString(stringResponse.ongkir().rates(),
                            new TypeToken<LogisticsRateQuery.Data.Rates>() {
                            }.getType()),

                            Data.class);
                    listener.onSuccess(data);

                } else {
                    throw new RuntimeException("Empty Response");
                }
            }
        };

        compositeSubscription.add(RxApollo.from(apolloWatcher)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void calculateKeroCartAddressShipping(
            @NonNull final Context context, @NonNull final TKPDMapParam<String, String> params,
            @NonNull final OnCalculateKeroAddressShipping listener) {

        ApolloWatcher<LogisticsRateQuery.Data> apolloWatcher = apolloClient.newCall(LogisticsRateQuery.builder()
                .input(getOngkirRatesInputFromParam(params))
                .build()
        ).watcher();

        Subscriber<LogisticsRateQuery.Data> subscriber = new Subscriber<LogisticsRateQuery.Data>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onFailure();
            }

            @Override
            public void onNext(LogisticsRateQuery.Data response) {
                if (response != null &&
                        response.ongkir().rates() != null) {
                    Data data = new Gson().fromJson(CacheUtil.convertModelToString(response.ongkir().rates(),
                            new TypeToken<LogisticsRateQuery.Data.Rates>() {
                            }.getType()), Data.class);

                    listener.onSuccess(data.getAttributes());
                } else {
                    listener.onFailure();
                }
            }
        };
        compositeSubscription.add(RxApollo.from(apolloWatcher).subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
    }

    private OngkirRatesInput getOngkirRatesInputFromParam(TKPDMapParam<String, String> params) {

        return OngkirRatesInput.builder()
                .cat_id(params.get(KeroppiParam.CAT_ID))
                .destination(params.get(KeroppiParam.DESTINATION))
                .from(params.get(KeroppiParam.FROM))
                .insurance(params.get(KeroppiParam.INSURANCE))
                .names(params.get(KeroppiParam.NAMES))
                .order_value(params.get(KeroppiParam.ORDER_VALUE))
                .origin(params.get(KeroppiParam.ORIGIN))
                .product_insurance(params.get(KeroppiParam.PRODUCT_INSURANCE))
                .token(params.get(KeroppiParam.TOKEN))
                .ut(params.get(KeroppiParam.UT))
                .weight(params.get(KeroppiParam.WEIGHT))
                .build();
    }
}
