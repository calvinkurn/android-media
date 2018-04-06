package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdAction;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailViewListener;
import com.tokopedia.topads.dashboard.view.model.Ad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public class TopAdsDetailProductViewPresenterImpl<T extends Ad> extends TopAdsDetailProductPresenterImpl implements TopAdsDetailProductPresenter {

    private TopAdsDetailViewListener<T> topAdsDetailViewListener;

    public TopAdsDetailProductViewPresenterImpl(Context context, TopAdsDetailViewListener<T> topAdsDetailViewListener, TopAdsProductAdInteractor productAdInteractor) {
        super(context, topAdsDetailViewListener, productAdInteractor);
        this.topAdsDetailViewListener = topAdsDetailViewListener;
    }

    @Override
    public void turnOnAds(String id) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        topAdsProductAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOnAdSuccess(dataResponseActionAds);
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onTurnOnAdError(throwable);
            }
        });
    }

    @Override
    public void turnOffAds(String id) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        topAdsProductAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOffAdSuccess(dataResponseActionAds);
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onTurnOffAdError();
            }
        });
    }

    @Override
    public void deleteAd(String id) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        topAdsProductAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onDeleteAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onDeleteAdError();
            }
        });
    }

    @NonNull
    private DataRequest<ProductAdBulkAction> generateActionRequest(String id, String action) {
        DataRequest<ProductAdBulkAction> dataRequest = new DataRequest<>();
        ProductAdBulkAction dataRequestSingleAd = new ProductAdBulkAction();
        dataRequestSingleAd.setAction(action);
        dataRequestSingleAd.setShopId(getShopId());
        List<ProductAdAction> dataRequestSingleAdses = new ArrayList<>();
        ProductAdAction data = new ProductAdAction();
        data.setId(id);
        dataRequestSingleAdses.add(data);
        dataRequestSingleAd.setAds(dataRequestSingleAdses);
        dataRequest.setData(dataRequestSingleAd);
        return dataRequest;
    }

}