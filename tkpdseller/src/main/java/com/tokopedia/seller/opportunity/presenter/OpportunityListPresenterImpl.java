package com.tokopedia.seller.opportunity.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.interactor.OpportunityNetworkInteractor;
import com.tokopedia.seller.opportunity.listener.OpportunityListView;
import com.tokopedia.seller.opportunity.viewmodel.OpportunityParam;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 3/2/17.
 */

public class OpportunityListPresenterImpl implements OpportunityListPresenter {

    private final OpportunityListView viewListener;
    private PagingHandler pagingHandler;
    private OpportunityNetworkInteractor networkInteractor;
    private CompositeSubscription compositeSubscription;
    private OpportunityParam opportunityParam;

    public OpportunityListPresenterImpl(OpportunityListView viewListener,
                                        OpportunityNetworkInteractor networkInteractor,
                                        CompositeSubscription compositeSubscription) {
        this.viewListener = viewListener;
        this.pagingHandler = new PagingHandler();
        this.networkInteractor = networkInteractor;
        this.compositeSubscription = compositeSubscription;
        this.opportunityParam = new OpportunityParam();
    }

    @Override
    public void getOpportunity() {
        viewListener.showLoadingList();
        CommonUtils.dumper("NISNIS " + getOpportunityParam().toString());
//        compositeSubscription.add(networkInteractor.getOpportunity(getOpportunityParam())
//                .subscribeOn(S
// chedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Response<TkpdResponse>>() {
//                    @Override
//                    public void onCompleted() {
//                        networkInteractor.setIsRequesting(false);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof UnknownHostException) {
//                            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.msg_no_connection));
//                        } else if (e instanceof SocketTimeoutException) {
//                            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_timeout));
//                        } else if (e instanceof IOException) {
//                            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_internal_server));
//                        } else {
//                            viewListener.onErrorGetOpportunity(viewListener.getString(R.string.default_request_error_unknown));
//                        }
//                    }
//
//                    @Override
//                    public void onNext(Response<TkpdResponse> response) {
//
//                        viewListener.onSuccessGetOpportunity();
//
//                    }
//                }));

        viewListener.onSuccessGetOpportunity();
    }

    private TKPDMapParam<String, String> getOpportunityParam() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("page", String.valueOf(pagingHandler.getPage()));
        if (opportunityParam.getSort() != null)
            param.put("sort", opportunityParam.getSort());
        if (opportunityParam.getShippingType() != null)
            param.put("shipping", opportunityParam.getShippingType());
        if (opportunityParam.getCategory() != null)
            param.put("category", opportunityParam.getCategory());
        if (opportunityParam.getQuery() != null)
            param.put("query", opportunityParam.getQuery());
        return param;
    }

    @Override
    public void loadMore(int lastItemPosition, int visibleItem) {
        if (hasNextPage() && isOnLastPosition(lastItemPosition, visibleItem) && canLoadMore()) {
            pagingHandler.nextPage();
            getOpportunity();
        }
    }

    @Override
    public void onRefresh() {
        pagingHandler.resetPage();
        viewListener.getAdapter().getList().clear();
        getOpportunity();
    }

    @Override
    public void onDestroyView() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    public void setParamQuery(String query) {
        opportunityParam.setQuery(query);
    }

    @Override
    public void setParamSort(String sortParam) {
        opportunityParam.setSort(sortParam);
    }

    @Override
    public void setParamCategory(String categoryParam) {
        opportunityParam.setCategory(categoryParam);
    }

    @Override
    public void getParamShippingType(String shippingParam) {
        opportunityParam.setShippingType(shippingParam);
    }

    private boolean hasNextPage() {
        return true;
//        return pagingHandler.CheckNextPage();
    }

    public boolean isOnLastPosition(int itemPosition, int visibleItem) {
        return itemPosition == visibleItem;
    }

    private boolean canLoadMore() {
        return !networkInteractor.isRequesting();
    }


}
