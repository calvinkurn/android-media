package com.tokopedia.topads.dashboard.domain.interactor;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsCacheDataSourceImpl;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;

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

public class TopAdsGroupAdInteractorImpl implements TopAdsGroupAdInteractor {

    private CompositeSubscription compositeSubscription;
    private TopAdsManagementService topAdsManagementService;
    private TopAdsCacheDataSourceImpl topAdsCacheDataSource;
    private Context context;

    public TopAdsGroupAdInteractorImpl(Context context) {
        this.context = context;
        compositeSubscription = new CompositeSubscription();
        topAdsManagementService = new TopAdsManagementService(new SessionHandler(context));
        topAdsCacheDataSource = new TopAdsCacheDataSourceImpl(context);
    }

    @Override
    public void searchAd(SearchAdRequest searchAdRequest, final ListenerInteractor<PageDataResponse<List<GroupAd>>> listener) {
        Observable<Response<PageDataResponse<List<GroupAd>>>> observable = topAdsManagementService.getApi().getGroupAd(searchAdRequest.getParams());
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<PageDataResponse<List<GroupAd>>>, Observable<PageDataResponse<List<GroupAd>>>>() {
                    @Override
                    public Observable<PageDataResponse<List<GroupAd>>> call(Response<PageDataResponse<List<GroupAd>>> response) {
                        return Observable.just(response.body());
                    }
                })
                .subscribe(new SubscribeOnNext<PageDataResponse<List<GroupAd>>>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void bulkAction(DataRequest<GroupAdBulkAction> bulkActionDataRequest, ListenerInteractor<GroupAdBulkAction> listener) {
        Observable<Response<DataResponse<GroupAdBulkAction>>> observable = topAdsManagementService.getApi().bulkActionGroupAd(bulkActionDataRequest);
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<DataResponse<GroupAdBulkAction>>, Observable<GroupAdBulkAction>>() {
                    @Override
                    public Observable<GroupAdBulkAction> call(Response<DataResponse<GroupAdBulkAction>> response) {
                        return Observable.just(response.body().getData());
                    }
                })
                .subscribe(new SubscribeOnNext<GroupAdBulkAction>(listener), new SubscribeOnError(listener)));
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
