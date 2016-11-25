package com.tokopedia.seller.topads.interactor;

import com.tokopedia.seller.topads.datasource.TopAdsDbDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.exchange.CreditResponse;
import com.tokopedia.seller.topads.model.exchange.DepositResponse;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;
import com.tokopedia.seller.topads.model.exchange.ShopResponse;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;
import com.tokopedia.seller.topads.model.exchange.StatisticResponse;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class DashboardTopadsInteractorImpl implements DashboardTopadsInteractor {

    private CompositeSubscription compositeSubscription;
    private TopAdsManagementService topAdsManagementService;
    private TopAdsDbDataSource mTopAdsDbDataSource;

    public DashboardTopadsInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        topAdsManagementService = new TopAdsManagementService();
        mTopAdsDbDataSource = new TopAdsDbDataSourceImpl();
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
    public void getDashboardSummary(final StatisticRequest statisticRequest, final Listener<Summary> listener) {
        Observable<Summary> getSummaryCacheObservable = mTopAdsDbDataSource.getSummary(statisticRequest);
        compositeSubscription.add(getSummaryCacheObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Summary, Observable<Summary>>() {
                    @Override
                    public Observable<Summary> call(Summary summary) {
                        if (summary != null) {
                            return Observable.just(summary);
                        }
                        Observable<Response<StatisticResponse>> statisticApiObservable = topAdsManagementService.getApi().getDashboardStatistic(statisticRequest.getParams());
                        return statisticApiObservable
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.newThread())
                                .flatMap(new Func1<Response<StatisticResponse>, Observable<Summary>>() {
                            @Override
                            public Observable<Summary> call(Response<StatisticResponse> statisticResponse) {
                                Observable<Summary> insertSummaryObservable = mTopAdsDbDataSource.insertSummary(statisticRequest, statisticResponse.body().getData().getSummary());
                                Observable<List<Cell>> insertCellListObservable = mTopAdsDbDataSource.insertCellList(statisticRequest, statisticResponse.body().getData().getCells());
                                return Observable.zip(insertSummaryObservable, insertCellListObservable, new Func2<Summary, List<Cell>, Summary>() {
                                    @Override
                                    public Summary call(Summary summary, List<Cell> cellList) {
                                        return summary;
                                    }
                                });
                            }
                        });
                    }
                }).subscribe(new SubscribeOnNext<Summary>(listener), new SubscribeOnError(listener)));
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
