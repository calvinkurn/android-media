package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.datasource.TopAdsCacheDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSource;
import com.tokopedia.seller.topads.datasource.TopAdsDbDataSourceImpl;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdAction;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.response.PageDataResponse;
import com.tokopedia.seller.topads.network.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public class TopAdsDetailProductPresenterImpl extends TopAdsDetailPresenterImpl implements TopAdsDetailProductPresenter {

    private TopAdsProductAdInteractor topAdsProductAdInteractor;

    public TopAdsDetailProductPresenterImpl(Context context, TopAdsDetailViewListener topAdsDetailViewListener, TopAdsProductAdInteractor topAdsProductAdInteractor) {
        super(context, topAdsDetailViewListener);
        this.topAdsProductAdInteractor = topAdsProductAdInteractor;
    }

    @Override
    public void turnOnAds(Ad ad, String shopId) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(ad, TopAdsNetworkConstant.ACTION_BULK_ON_AD, shopId);
        actionBulkAds(dataRequest);
    }

    @Override
    public void turnOffAds(Ad ad, String shopId) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(ad, TopAdsNetworkConstant.ACTION_BULK_OFF_AD, shopId);
        actionBulkAds(dataRequest);
    }

    @NonNull
    private void actionBulkAds(DataRequest<ProductAdBulkAction> actionRequest){
        topAdsDetailViewListener.showProgress();
        topAdsProductAdInteractor.bulkAction(actionRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.dismissProgress();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.dismissProgress();
            }
        });
    }


    @NonNull
    private DataRequest<ProductAdBulkAction> generateActionRequest(Ad ad, String action, String shopId) {
        DataRequest<ProductAdBulkAction> dataRequest = new DataRequest<>();
        ProductAdBulkAction dataRequestSingleAd = new ProductAdBulkAction();
        dataRequestSingleAd.setAction(action);
        dataRequestSingleAd.setShopId(shopId);
        List<ProductAdAction> dataRequestSingleAdses = new ArrayList<>();
        ProductAdAction data = new ProductAdAction();
        data.setId(String.valueOf(ad.getId()));
        dataRequestSingleAdses.add(data);
        dataRequestSingleAd.setAds(dataRequestSingleAdses);
        dataRequest.setData(dataRequestSingleAd);
        return dataRequest;
    }

}
