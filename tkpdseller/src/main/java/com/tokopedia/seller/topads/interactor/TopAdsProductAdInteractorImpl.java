package com.tokopedia.seller.topads.interactor;

import android.content.Context;

import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSourceImpl;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
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
 * Created by Nathaniel on 12/29/2016.
 */

public class TopAdsProductAdInteractorImpl implements TopAdsProductAdInteractor {

    private CompositeSubscription compositeSubscription;
    private TopAdsManagementService topAdsManagementService;
    private TopAdsDbDataSource topAdsDbDataSource;
    private TopAdsCacheDataSourceImpl topAdsCacheDataSource;
    private Context context;

    public TopAdsProductAdInteractorImpl(Context context) {
        this.context = context;
        compositeSubscription = new CompositeSubscription();
        topAdsManagementService = new TopAdsManagementService();
        topAdsDbDataSource = new TopAdsDbDataSourceImpl();
        topAdsCacheDataSource = new TopAdsCacheDataSourceImpl(context);
    }

    @Override
    public void searchAd(SearchAdRequest searchAdRequest, final ListenerInteractor<PageDataResponse<List<ProductAd>>> listener) {
        Observable<Response<PageDataResponse<List<ProductAd>>>> observable = topAdsManagementService.getApi().searchProductAd(searchAdRequest.getParams());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<PageDataResponse<List<ProductAd>>>, Observable<PageDataResponse<List<ProductAd>>>>() {
                    @Override
                    public Observable<PageDataResponse<List<ProductAd>>> call(Response<PageDataResponse<List<ProductAd>>> depositResponseResponse) {
                        return Observable.just(depositResponseResponse.body());
                    }
                })
                .subscribe(new SubscribeOnNext<PageDataResponse<List<ProductAd>>>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void bulkAction(DataRequest<ProductAdBulkAction> bulkActionDataRequest, ListenerInteractor<ProductAdBulkAction> listener) {

    }
}
