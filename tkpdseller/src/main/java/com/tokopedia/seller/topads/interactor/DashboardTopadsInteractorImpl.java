package com.tokopedia.seller.topads.interactor;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.DataRequestGroupAd;
import com.tokopedia.seller.topads.model.data.DataRequestSingleAd;
import com.tokopedia.seller.topads.model.data.DataResponseActionAds;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.request.AdsActionRequest;
import com.tokopedia.seller.topads.model.response.CreditResponse;
import com.tokopedia.seller.topads.model.response.GroupAdResponse;
import com.tokopedia.seller.topads.model.response.ActionAdsResponse;
import com.tokopedia.seller.topads.model.request.SearchProductRequest;
import com.tokopedia.seller.topads.model.response.SearchProductResponse;
import com.tokopedia.seller.topads.model.request.ShopRequest;
import com.tokopedia.seller.topads.model.response.DepositResponse;
import com.tokopedia.seller.topads.model.response.ProductResponse;
import com.tokopedia.seller.topads.model.response.ShopResponse;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.model.response.StatisticResponse;
import com.tokopedia.seller.topads.model.response.TotalAdResponse;
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
    private TopAdsDbDataSource topAdsDbDataSource;
    private TopAdsCacheDataSourceImpl topAdsCacheDataSource;
    private Context context;

    public DashboardTopadsInteractorImpl(Context context) {
        this.context = context;
        compositeSubscription = new CompositeSubscription();
        topAdsManagementService = new TopAdsManagementService();
        topAdsDbDataSource = new TopAdsDbDataSourceImpl();
        topAdsCacheDataSource = new TopAdsCacheDataSourceImpl(context);
    }

    @Override
    public void getDashboardSummary(final StatisticRequest statisticRequest, final ListenerInteractor<Summary> listener) {
        Observable<Summary> getSummaryCacheObservable = topAdsDbDataSource.getSummary(statisticRequest);
        compositeSubscription.add(getSummaryCacheObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Summary, Observable<Summary>>() {
                    @Override
                    public Observable<Summary> call(Summary summary) {
                        if (!topAdsCacheDataSource.isStatisticDataExpired() && summary != null) {
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
                                        Observable<Summary> insertSummaryObservable = topAdsDbDataSource.insertSummary(statisticRequest, statisticResponse.body().getData().getSummary());
                                        Observable<List<Cell>> insertCellListObservable = topAdsDbDataSource.insertCellList(statisticRequest, statisticResponse.body().getData().getCells());
                                        return Observable.zip(insertSummaryObservable, insertCellListObservable, new Func2<Summary, List<Cell>, Summary>() {
                                            @Override
                                            public Summary call(Summary summary, List<Cell> cellList) {
                                                topAdsCacheDataSource.updateLastInsertStatistic();
                                                return summary;
                                            }
                                        });
                                    }
                                });
                    }
                }).subscribe(new SubscribeOnNext<Summary>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void getDeposit(ShopRequest shopRequest, ListenerInteractor<DataDeposit> listener) {
        Observable<Response<DepositResponse>> depositObservable = topAdsManagementService.getApi().getDashboardDeposit(shopRequest.getParams());
        compositeSubscription.add(depositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<DepositResponse>, Observable<DataDeposit>>() {
                    @Override
                    public Observable<DataDeposit> call(Response<DepositResponse> depositResponseResponse) {
                        return Observable.just(depositResponseResponse.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<DataDeposit>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void getShopInfo(ShopRequest shopRequest, ListenerInteractor<ShopModel> listener) {
        ShopService shopService = new ShopService();
        Observable<Response<TkpdResponse>> observable = shopService.getApi().getShopInfo(AuthUtil.generateParams(context, shopRequest.getParams()));
        compositeSubscription.add(observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<ShopModel>>() {
                    @Override
                    public Observable<ShopModel> call(Response<TkpdResponse> tkpdResponse) {
                        ShopModel shopModel = new Gson().fromJson(tkpdResponse.body().getStringData(), ShopModel.class);
                        return Observable.just(shopModel);
                    }
                })
                .subscribe(new SubscribeOnNext<ShopModel>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void getTotalAd(ShopRequest shopRequest, ListenerInteractor<TotalAd> listener) {
        Observable<Response<TotalAdResponse>> depositObservable = topAdsManagementService.getApi().getDashboardTotalAd(shopRequest.getParams());
        compositeSubscription.add(depositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<TotalAdResponse>, Observable<TotalAd>>() {
                    @Override
                    public Observable<TotalAd> call(Response<TotalAdResponse> totalAdResponse) {
                        return Observable.just(totalAdResponse.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<TotalAd>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void getCreditList(ListenerInteractor<List<DataCredit>> listener) {
        Observable<Response<CreditResponse>> depositObservable = topAdsManagementService.getApi().getDashboardCredit();
        compositeSubscription.add(depositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<CreditResponse>, Observable<List<DataCredit>>>() {
                    @Override
                    public Observable<List<DataCredit>> call(Response<CreditResponse> response) {
                        return Observable.just(response.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<List<DataCredit>>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void searchProduct(SearchProductRequest searchProductRequest, ListenerInteractor<List<Product>> listener) {
        Observable<Response<SearchProductResponse>> depositObservable = topAdsManagementService.getApi().getSearchProduct(searchProductRequest.getParams());
        compositeSubscription.add(depositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<SearchProductResponse>, Observable<List<Product>>>() {
                    @Override
                    public Observable<List<Product>> call(Response<SearchProductResponse> response) {
                        return Observable.just(response.body().getProductList());
                    }
                })
                .subscribe(new SubscribeOnNext<List<Product>>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void getListProductAds(HashMap<String, String> params, final ListenerInteractor<ProductResponse> listener) {
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
    public void getListGroupAds(HashMap<String, String> params, final ListenerInteractor<GroupAdResponse> listener) {
        Observable<Response<GroupAdResponse>> observable = topAdsManagementService.getApi()
                .getDashboardGroup(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<GroupAdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<GroupAdResponse> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void getDashboardShop(HashMap<String, String> params, final ListenerInteractor<ShopResponse> listener) {
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
    public void getDashboardResponse(HashMap<String, String> params, final ListenerInteractor<DepositResponse> listener) {
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
    public void actionSingleAds(AdsActionRequest<DataRequestSingleAd> adsActionRequest, ListenerInteractor<DataResponseActionAds> listenerInteractor) {
        Observable<Response<ActionAdsResponse>> actionAdsObservable = topAdsManagementService.getApi().postActionSingleAds(adsActionRequest);
        compositeSubscription.add(actionAdsObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<ActionAdsResponse>, Observable<DataResponseActionAds>>() {
                    @Override
                    public Observable<DataResponseActionAds> call(Response<ActionAdsResponse> responseActionAdsResponse) {
                        return Observable.just(responseActionAdsResponse.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<DataResponseActionAds>(listenerInteractor), new SubscribeOnError(listenerInteractor)));
    }

    @Override
    public void actionGroupAds(AdsActionRequest<DataRequestGroupAd> adsActionRequest, ListenerInteractor<DataResponseActionAds> listenerInteractor) {

    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
