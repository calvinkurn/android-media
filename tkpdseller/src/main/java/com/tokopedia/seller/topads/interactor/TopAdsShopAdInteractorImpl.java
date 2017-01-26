package com.tokopedia.seller.topads.interactor;

import android.content.Context;

import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.model.data.ShopAd;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.response.DataResponse;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nathaniel on 12/29/2016.
 */

public class TopAdsShopAdInteractorImpl implements TopAdsShopAdInteractor {

    private CompositeSubscription compositeSubscription;
    private TopAdsManagementService topAdsManagementService;
    private TopAdsDbDataSource topAdsDbDataSource;
    private TopAdsCacheDataSourceImpl topAdsCacheDataSource;
    private Context context;

    public TopAdsShopAdInteractorImpl(Context context) {
        this.context = context;
        compositeSubscription = new CompositeSubscription();
        topAdsManagementService = new TopAdsManagementService();
        topAdsDbDataSource = new TopAdsDbDataSourceImpl();
        topAdsCacheDataSource = new TopAdsCacheDataSourceImpl(context);
    }

    @Override
    public void getShopAd(SearchAdRequest searchAdRequest, ListenerInteractor<ShopAd> listener) {
        Observable<Response<DataResponse<ShopAd>>> observable = topAdsManagementService.getApi().getShopAd(searchAdRequest.getShopParams());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<DataResponse<ShopAd>>, Observable<ShopAd>>() {
                    @Override
                    public Observable<ShopAd> call(Response<DataResponse<ShopAd>> response) {
                        return Observable.just(response.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<ShopAd>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
