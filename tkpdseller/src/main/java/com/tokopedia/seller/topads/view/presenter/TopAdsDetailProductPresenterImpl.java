package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.domain.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.seller.topads.data.model.data.ProductAd;
import com.tokopedia.seller.topads.data.model.data.ProductAdAction;
import com.tokopedia.seller.topads.data.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.data.model.request.DataRequest;
import com.tokopedia.seller.topads.data.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public class TopAdsDetailProductPresenterImpl extends TopAdsDetailPresenterImpl implements TopAdsDetailProductPresenter {

    private TopAdsProductAdInteractor productAdInteractor;

    public TopAdsDetailProductPresenterImpl(Context context, TopAdsDetailViewListener topAdsDetailViewListener, TopAdsProductAdInteractor productAdInteractor) {
        super(context, topAdsDetailViewListener);
        this.productAdInteractor = productAdInteractor;
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, String id) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setAdId(id);
        productAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<ProductAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<ProductAd>> pageDataResponse) {
                topAdsDetailViewListener.onAdLoaded(pageDataResponse.getData().get(0));
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onLoadAdError();
            }
        });
    }

    @Override
    public void turnOnAds(String id) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        productAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOnAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onTurnOnAdError();
            }
        });
    }

    @Override
    public void turnOffAds(String id) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        productAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOffAdSuccess();
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
        productAdInteractor.bulkAction(dataRequest, new ListenerInteractor<ProductAdBulkAction>() {
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

    @Override
    public void unSubscribe() {
        if (productAdInteractor != null) {
            productAdInteractor.unSubscribe();
        }
    }

}