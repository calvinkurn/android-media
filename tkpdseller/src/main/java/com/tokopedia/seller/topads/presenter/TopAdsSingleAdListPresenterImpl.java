package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.data.ProductAdAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public class TopAdsSingleAdListPresenterImpl extends TopAdsAdListPresenterImpl<ProductAd> implements TopAdsSingleAdListPresenter {
    public TopAdsSingleAdListPresenterImpl(Context context, TopAdsListPromoViewListener topadsListPromoViewListener) {
        super(context, topadsListPromoViewListener);
    }

    @Override
    public void getListTopAdsFromNet(Date startDate, Date endDate) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setShopId(getShopId());
//        dashboardTopadsInteractor.getListProductAds(searchAdRequest, new ListenerInteractor<PageDataResponse<List<ProductAd>>>(){
//
//            @Override
//            public void onSuccess(PageDataResponse<List<ProductAd>> productResponse) {
//                if(productResponse != null) {
//                    topAdsListItem.addAll(productResponse.getData());
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//        });
    }


    @Override
    public void actionDeleteAds(List<ProductAd> ads) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        actionBulkAds(dataRequest);
    }

    @Override
    public void actionOffAds(List<ProductAd> ads) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        actionBulkAds(dataRequest);
    }

    @Override
    public void actionOnAds(List<ProductAd> ads) {
        DataRequest<ProductAdBulkAction> dataRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        actionBulkAds(dataRequest);
    }

    @NonNull
    private void actionBulkAds(DataRequest<ProductAdBulkAction> actionRequest){
        dashboardTopadsInteractor.actionSingleAds(actionRequest, new ListenerInteractor<ProductAdBulkAction>() {
            @Override
            public void onSuccess(ProductAdBulkAction dataResponseActionAds) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @NonNull
    private DataRequest<ProductAdBulkAction> generateActionRequest(List<ProductAd> ads, String action) {
        DataRequest<ProductAdBulkAction> dataRequest = new DataRequest<>();
        ProductAdBulkAction dataRequestSingleAd = new ProductAdBulkAction();
        dataRequestSingleAd.setAction(action);
        dataRequestSingleAd.setShopId(SessionHandler.getShopID(context));
        List<ProductAdAction> dataRequestSingleAdses = new ArrayList<>();
        for(ProductAd ad : ads){
            ProductAdAction data = new ProductAdAction();
            data.setId(ad.getId());
            dataRequestSingleAdses.add(data);
        }
        dataRequestSingleAd.setAds(dataRequestSingleAdses);
        dataRequest.setData(dataRequestSingleAd);
        return dataRequest;
    }

}
