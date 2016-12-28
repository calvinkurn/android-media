package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.SingleAd;
import com.tokopedia.seller.topads.model.data.DataRequestSingleAd;
import com.tokopedia.seller.topads.model.data.DataRequestSingleAds;
import com.tokopedia.seller.topads.model.data.DataResponseActionAds;
import com.tokopedia.seller.topads.model.request.AdsActionRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public class TopAdsSingleAdListPresenterImpl extends TopAdsAdListPresenterImpl<SingleAd> implements TopAdsSingleAdListPresenter {
    public TopAdsSingleAdListPresenterImpl(Context context, TopAdsListPromoViewListener topadsListPromoViewListener) {
        super(context, topadsListPromoViewListener);
    }

    @Override
    public void getListTopAdsFromNet(Date startDate, Date endDate) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setShopId(getShopId());
//        dashboardTopadsInteractor.getListProductAds(searchAdRequest, new ListenerInteractor<PageDataResponse<List<SingleAd>>>(){
//
//            @Override
//            public void onSuccess(PageDataResponse<List<SingleAd>> productResponse) {
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
    public void actionDeleteAds(List<SingleAd> ads) {
        AdsActionRequest<DataRequestSingleAd> adsActionRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        actionBulkAds(adsActionRequest);
    }

    @Override
    public void actionOffAds(List<SingleAd> ads) {
        AdsActionRequest<DataRequestSingleAd> adsActionRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        actionBulkAds(adsActionRequest);
    }

    @Override
    public void actionOnAds(List<SingleAd> ads) {
        AdsActionRequest<DataRequestSingleAd> adsActionRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        actionBulkAds(adsActionRequest);
    }

    @NonNull
    private void actionBulkAds(AdsActionRequest<DataRequestSingleAd> actionRequest){
        dashboardTopadsInteractor.actionSingleAds(actionRequest, new ListenerInteractor<DataResponseActionAds>() {
            @Override
            public void onSuccess(DataResponseActionAds dataResponseActionAds) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @NonNull
    private AdsActionRequest<DataRequestSingleAd> generateActionRequest(List<SingleAd> ads, String action) {
        AdsActionRequest<DataRequestSingleAd> adsActionRequest = new AdsActionRequest<>();
        DataRequestSingleAd dataRequestSingleAd = new DataRequestSingleAd();
        dataRequestSingleAd.setAction(action);
        dataRequestSingleAd.setShopId(SessionHandler.getShopID(context));
        List<DataRequestSingleAds> dataRequestSingleAdses = new ArrayList<>();
        for(SingleAd ad : ads){
            DataRequestSingleAds data = new DataRequestSingleAds();
            data.setAdId(String.valueOf(ad.getId()));
            dataRequestSingleAdses.add(data);
        }
        dataRequestSingleAd.setAds(dataRequestSingleAdses);
        adsActionRequest.setData(dataRequestSingleAd);
        return adsActionRequest;
    }

}
