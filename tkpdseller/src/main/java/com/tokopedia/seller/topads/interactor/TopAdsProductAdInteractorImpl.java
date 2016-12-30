package com.tokopedia.seller.topads.interactor;

import android.content.Context;

import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.Product;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.response.DataResponse;
import com.tokopedia.seller.topads.model.response.PageDataResponse;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 12/30/16.
 */

public class TopAdsProductAdInteractorImpl implements TopAdsProductAdInteractor {

    private CompositeSubscription compositeSubscription;
    private TopAdsManagementService topAdsManagementService;
    private TopAdsDbDataSource topAdsDbDataSource;
    private TopAdsCacheDataSource topAdsCacheDataSource;

    public TopAdsProductAdInteractorImpl(CompositeSubscription compositeSubscription, TopAdsManagementService topAdsManagementService, TopAdsDbDataSource topAdsDbDataSource, TopAdsCacheDataSource topAdsCacheDataSource) {
        this.compositeSubscription = compositeSubscription;
        this.topAdsManagementService = topAdsManagementService;
        this.topAdsDbDataSource = topAdsDbDataSource;
        this.topAdsCacheDataSource = topAdsCacheDataSource;
    }

    @Override
    public void searchAd(SearchAdRequest searchAdRequest, ListenerInteractor<List<ProductAd>> listener) {

    }

    @Override
    public void bulkAction(DataRequest<ProductAdBulkAction> bulkActionDataRequest, ListenerInteractor<ProductAdBulkAction> listener) {

    }

    @Override
    public void getDetailProductAd(ListenerInteractor<List<ProductAd>> listenerInteractor) {
        Observable<Response<PageDataResponse<List<ProductAd>>>> detailAdObservable = topAdsManagementService.getApi().getDetailProductAd();
        compositeSubscription.add(detailAdObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .flatMap(new Func1<Response<PageDataResponse<List<ProductAd>>>, Observable<List<ProductAd>>>() {
                    @Override
                    public Observable<List<ProductAd>> call(Response<PageDataResponse<List<ProductAd>>> pageDataResponseResponse) {
                        return Observable.just(pageDataResponseResponse.body().getData());
                    }

                })
                .subscribe(new SubscribeOnNext<List<ProductAd>>(listenerInteractor), new SubscribeOnError(listenerInteractor)));
    }
}
