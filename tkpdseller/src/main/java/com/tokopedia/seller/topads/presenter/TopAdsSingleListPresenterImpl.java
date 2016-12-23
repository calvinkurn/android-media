package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.DataRequestSingleAd;
import com.tokopedia.seller.topads.model.data.DataRequestSingleAds;
import com.tokopedia.seller.topads.model.data.DataResponseActionAds;
import com.tokopedia.seller.topads.model.exchange.AdsActionRequest;
import com.tokopedia.seller.topads.model.exchange.ProductResponse;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public class TopAdsSingleListPresenterImpl extends TopAdsListPresenterImpl<Ad> implements TopAdsSingleListPresenter {
    public TopAdsSingleListPresenterImpl(Context context, TopAdsListPromoViewListener topadsListPromoViewListener) {
        super(context, topadsListPromoViewListener);
    }

    @Override
    public void getListTopAdsFromNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put(TopAdsNetworkConstant.PARAM_SHOP_ID, SessionHandler.getShopID(context));
        params.put(TopAdsNetworkConstant.PARAM_START_DATE, "");
        params.put(TopAdsNetworkConstant.PARAM_END_DATE, "");
        dashboardTopadsInteractor.getListProductAds(params, new ListenerInteractor<ProductResponse>(){

            @Override
            public void onSuccess(ProductResponse productResponse) {
                if(productResponse != null) {
                    topAdsListItem.addAll(productResponse.getData());
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }


    @Override
    public void actionDeleteAds(List<Ad> ads) {
        AdsActionRequest<DataRequestSingleAd> adsActionRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        actionBulkAds(adsActionRequest);
    }

    @Override
    public void actionOffAds(List<Ad> ads) {
        AdsActionRequest<DataRequestSingleAd> adsActionRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        actionBulkAds(adsActionRequest);
    }

    @Override
    public void actionOnAds(List<Ad> ads) {
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
    private AdsActionRequest<DataRequestSingleAd> generateActionRequest(List<Ad> ads, String action) {
        AdsActionRequest<DataRequestSingleAd> adsActionRequest = new AdsActionRequest<>();
        DataRequestSingleAd dataRequestSingleAd = new DataRequestSingleAd();
        dataRequestSingleAd.setAction(action);
        dataRequestSingleAd.setShopId(SessionHandler.getShopID(context));
        List<DataRequestSingleAds> dataRequestSingleAdses = new ArrayList<>();
        for(Ad ad : ads){
            DataRequestSingleAds data = new DataRequestSingleAds();
            data.setAdId(String.valueOf(ad.getAdId()));
            dataRequestSingleAdses.add(data);
        }
        dataRequestSingleAd.setAds(dataRequestSingleAdses);
        adsActionRequest.setData(dataRequestSingleAd);
        return adsActionRequest;
    }

}
