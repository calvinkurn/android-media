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

    private CompositeSubscription compositeSubscription;
    private TopAdsManagementService topAdsManagementService;

    public DashboardTopadsInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        topAdsManagementService = new TopAdsManagementService();
    }

    @Override
    public void getDashboardProduct(HashMap<String, String> params, final Listener<ProductResponse> listener) {
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
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<ProductResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void getDashboardShop(HashMap<String, String> params, final Listener<ShopResponse> listener) {
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
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<ShopResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void getDashboardStatistic(HashMap<String, String> params, final Listener<StatisticResponse> listener) {
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
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<StatisticResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void getDashboardResponse(HashMap<String, String> params, final Listener<DepositResponse> listener) {
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
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<DepositResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void getDashboardCredit(HashMap<String, String> params, final Listener<CreditResponse> listener) {
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
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<CreditResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
