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
import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.DataStatistic;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.request.SearchProductRequest;
import com.tokopedia.seller.topads.model.request.ShopRequest;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.model.response.DataResponse;
import com.tokopedia.seller.topads.model.response.PageDataResponse;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;

import java.util.Date;
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
    public void resetDate() {
        topAdsCacheDataSource.resetDate();
    }

    @Override
    public void saveDate(Date startDate, Date endDate) {
        topAdsCacheDataSource.saveDate(startDate, endDate);
    }

    @Override
    public Date getStartDate(Date defaultDate) {
        return topAdsCacheDataSource.getStartDate(defaultDate);
    }

    @Override
    public Date getEndDate(Date defaultDate) {
        return topAdsCacheDataSource.getEndDate(defaultDate);
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
                        Observable<Response<DataResponse<DataStatistic>>> statisticApiObservable = topAdsManagementService.getApi().getDashboardStatistic(statisticRequest.getParams());
                        return statisticApiObservable
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.newThread())
                                .flatMap(new Func1<Response<DataResponse<DataStatistic>>, Observable<Summary>>() {
                                    @Override
                                    public Observable<Summary> call(Response<DataResponse<DataStatistic>> statisticResponse) {
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
        Observable<Response<DataResponse<DataDeposit>>> depositObservable = topAdsManagementService.getApi().getDashboardDeposit(shopRequest.getParams());
        compositeSubscription.add(depositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<DataResponse<DataDeposit>>, Observable<DataDeposit>>() {
                    @Override
                    public Observable<DataDeposit> call(Response<DataResponse<DataDeposit>> depositResponseResponse) {
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
        Observable<Response<DataResponse<TotalAd>>> depositObservable = topAdsManagementService.getApi().getDashboardTotalAd(shopRequest.getParams());
        compositeSubscription.add(depositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<DataResponse<TotalAd>>, Observable<TotalAd>>() {
                    @Override
                    public Observable<TotalAd> call(Response<DataResponse<TotalAd>> totalAdResponse) {
                        return Observable.just(totalAdResponse.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<TotalAd>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void getCreditList(ListenerInteractor<List<DataCredit>> listener) {
        Observable<Response<DataResponse<List<DataCredit>>>> depositObservable = topAdsManagementService.getApi().getDashboardCredit();
        compositeSubscription.add(depositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<DataResponse<List<DataCredit>>>, Observable<List<DataCredit>>>() {
                    @Override
                    public Observable<List<DataCredit>> call(Response<DataResponse<List<DataCredit>>> response) {
                        return Observable.just(response.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<List<DataCredit>>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void searchProduct(SearchProductRequest searchProductRequest, ListenerInteractor<List<Product>> listener) {
        Observable<Response<DataResponse<List<Product>>>> depositObservable = topAdsManagementService.getApi().searchProduct(searchProductRequest.getParams());
        compositeSubscription.add(depositObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<DataResponse<List<Product>>>, Observable<List<Product>>>() {
                    @Override
                    public Observable<List<Product>> call(Response<DataResponse<List<Product>>> response) {
                        return Observable.just(response.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<List<Product>>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void getListProductAds(HashMap<String, String> params, final ListenerInteractor<PageDataResponse<List<ProductAd>>> listener) {
        Observable<Response<PageDataResponse<List<ProductAd>>>> observable = topAdsManagementService.getApi()
                .searchProductAd(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<PageDataResponse<List<ProductAd>>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<PageDataResponse<List<ProductAd>>> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void getListGroupAds(SearchAdRequest searchAdRequest, final ListenerInteractor<List<GroupAd>> listener) {
        Observable<Response<PageDataResponse<List<GroupAd>>>> observable = topAdsManagementService.getApi().searchGroupAd(searchAdRequest.getParams());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<PageDataResponse<List<GroupAd>>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<PageDataResponse<List<GroupAd>>> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body().getData());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void getDashboardShop(HashMap<String, String> params, final ListenerInteractor<DataResponse<ProductAd>> listener) {
        Observable<Response<DataResponse<ProductAd>>> observable = topAdsManagementService.getApi().getDashboardShop(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<DataResponse<ProductAd>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<DataResponse<ProductAd>> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void getDashboardResponse(HashMap<String, String> params, final ListenerInteractor<DataResponse<DataDeposit>> listener) {
        Observable<Response<DataResponse<DataDeposit>>> observable = topAdsManagementService.getApi().getDashboardDeposit(params);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<DataResponse<DataDeposit>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(Response<DataResponse<DataDeposit>> response) {
                        if (response.isSuccessful()) {
                            listener.onSuccess(response.body());
                        } else {
                            // TODO Define error
                        }
                    }
                }));
    }

    @Override
    public void actionSingleAds(DataRequest<ProductAdBulkAction> dataRequest, ListenerInteractor<ProductAdBulkAction> listenerInteractor) {
        Observable<Response<DataResponse<ProductAdBulkAction>>> actionAdsObservable = topAdsManagementService.getApi().bulkActionProductAd(dataRequest);
        compositeSubscription.add(actionAdsObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<DataResponse<ProductAdBulkAction>>, Observable<ProductAdBulkAction>>() {
                    @Override
                    public Observable<ProductAdBulkAction> call(Response<DataResponse<ProductAdBulkAction>> responseActionAdsResponse) {
                        return Observable.just(responseActionAdsResponse.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<ProductAdBulkAction>(listenerInteractor), new SubscribeOnError(listenerInteractor)));
    }

    @Override
    public void actionGroupAds(DataRequest<GroupAdBulkAction> dataRequest, ListenerInteractor<GroupAdBulkAction> listenerInteractor) {

    }


    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
