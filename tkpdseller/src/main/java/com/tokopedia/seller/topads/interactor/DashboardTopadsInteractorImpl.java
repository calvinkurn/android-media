package com.tokopedia.seller.topads.interactor;

import android.content.Context;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.model.exchange.CreditResponse;
import com.tokopedia.seller.topads.model.exchange.DepositResponse;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;
import com.tokopedia.seller.topads.model.exchange.ShopResponse;
import com.tokopedia.seller.topads.model.exchange.StatisticResponse;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class DashboardTopadsInteractorImpl implements DashboardTopadsInteractor {
    CompositeSubscription compositeSubscription;
    TopAdsManagementService topAdsManagementService;

    public DashboardTopadsInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        topAdsManagementService = new TopAdsManagementService();
    }

    @Override
    public void getDashboardProduct(final Context context, HashMap<String, String> params, final ListenerGetDashboardProduct listenerGetDashboardProduct) {
        Observable<Response<ProductResponse>> observable = topAdsManagementService.getApi()
                .getDashboardProduct(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ProductResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listenerGetDashboardProduct.onError(context.getString(R.string.msg_no_connection));
                        } else if (e instanceof SocketTimeoutException) {
                            listenerGetDashboardProduct.onError(context.getString(R.string.default_request_error_timeout));
                        } else {
                            listenerGetDashboardProduct.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(Response<ProductResponse> response) {
                        if (response.isSuccessful()) {
                            // do something
                        } else {
                            listenerGetDashboardProduct.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }
                }));
    }

    @Override
    public void getDashboardShop(final Context context, HashMap<String, String> params, final ListenerGetDashboardShop listenerGetDashboardShop) {
        Observable<Response<ShopResponse>> observable = topAdsManagementService.getApi().getDashboardShop(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ShopResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listenerGetDashboardShop.onError(context.getString(R.string.msg_no_connection));
                        } else if (e instanceof SocketTimeoutException) {
                            listenerGetDashboardShop.onError(context.getString(R.string.default_request_error_timeout));
                        } else {
                            listenerGetDashboardShop.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(Response<ShopResponse> response) {
                        if (response.isSuccessful()) {
                            // do something
                        } else {
                            listenerGetDashboardShop.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }
                }));
    }

    @Override
    public void getDashboardStatistic(final Context context, HashMap<String, String> params, final ListenerGetDashboardStatistic listenerGetDashboardStatistic) {
        Observable<Response<StatisticResponse>> observable = topAdsManagementService.getApi().getDashboardStatistic(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<StatisticResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listenerGetDashboardStatistic.onError(context.getString(R.string.msg_no_connection));
                        } else if (e instanceof SocketTimeoutException) {
                            listenerGetDashboardStatistic.onError(context.getString(R.string.default_request_error_timeout));
                        } else {
                            listenerGetDashboardStatistic.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(Response<StatisticResponse> response) {
                        if (response.isSuccessful()) {
                            // do something
                        } else {
                            listenerGetDashboardStatistic.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }
                }));
    }

    @Override
    public void getDashboardResponse(final Context context, HashMap<String, String> params, final ListenerGetDashboardDeposit listenerGetDashboardDeposit) {
        Observable<Response<DepositResponse>> observable = topAdsManagementService.getApi().getDashboardDeposit(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<DepositResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listenerGetDashboardDeposit.onError(context.getString(R.string.msg_no_connection));
                        } else if (e instanceof SocketTimeoutException) {
                            listenerGetDashboardDeposit.onError(context.getString(R.string.default_request_error_timeout));
                        } else {
                            listenerGetDashboardDeposit.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(Response<DepositResponse> response) {
                        if (response.isSuccessful()) {
                            // do something
                        } else {
                            listenerGetDashboardDeposit.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }
                }));
    }

    @Override
    public void getDashboardCredit(final Context context, HashMap<String, String> params, final ListenerGetDashboardCredit listenerGetDashboardCredit) {
        Observable<Response<CreditResponse>> observable = topAdsManagementService.getApi().getDashboardCredit(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<CreditResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            listenerGetDashboardCredit.onError(context.getString(R.string.msg_no_connection));
                        } else if (e instanceof SocketTimeoutException) {
                            listenerGetDashboardCredit.onError(context.getString(R.string.default_request_error_timeout));
                        } else {
                            listenerGetDashboardCredit.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(Response<CreditResponse> response) {
                        if (response.isSuccessful()) {
                            // do something
                            listenerGetDashboardCredit.onSuccess(response.body());
                        } else {
                            listenerGetDashboardCredit.onError(context.getString(R.string.default_request_error_unknown));
                        }
                    }
                }));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
