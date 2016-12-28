package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.DataRequestGroupAd;
import com.tokopedia.seller.topads.model.data.DataRequestGroupAds;
import com.tokopedia.seller.topads.model.data.DataResponseActionAds;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.request.AdsActionRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupListPresenterImpl extends TopAdsListPresenterImpl<GroupAd> implements TopAdsGroupListPresenter {

    public TopAdsGroupListPresenterImpl(Context context, TopAdsListPromoViewListener topAdsListPromoViewListener) {
        super(context, topAdsListPromoViewListener);
    }

    @Override
    public void getListTopAdsFromNet(Date startDate, Date endDate) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setShopId(getShopId());
        dashboardTopadsInteractor.getListGroupAds(searchAdRequest, new ListenerInteractor<List<GroupAd>>() {

            @Override
            public void onSuccess(List<GroupAd> groupAdList) {
                
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @Override
    public void actionDeleteAds(List<GroupAd> ads) {
        AdsActionRequest<DataRequestGroupAd> adsActionRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        actionBulkAds(adsActionRequest);
    }

    @Override
    public void actionOffAds(List<GroupAd> ads) {
        AdsActionRequest<DataRequestGroupAd> adsActionRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        actionBulkAds(adsActionRequest);
    }

    @Override
    public void actionOnAds(List<GroupAd> ads) {
        AdsActionRequest<DataRequestGroupAd> adsActionRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        actionBulkAds(adsActionRequest);
    }

    @NonNull
    private void actionBulkAds(AdsActionRequest<DataRequestGroupAd> actionRequest) {
        dashboardTopadsInteractor.actionGroupAds(actionRequest, new ListenerInteractor<DataResponseActionAds>() {
            @Override
            public void onSuccess(DataResponseActionAds dataResponseActionAds) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @NonNull
    private AdsActionRequest<DataRequestGroupAd> generateActionRequest(List<GroupAd> ads, String action) {
        AdsActionRequest<DataRequestGroupAd> adsActionRequest = new AdsActionRequest<>();
        DataRequestGroupAd dataRequestGroupAd = new DataRequestGroupAd();
        dataRequestGroupAd.setAction(action);
        dataRequestGroupAd.setShopId(SessionHandler.getShopID(context));
        List<DataRequestGroupAds> dataRequestGroupAdses = new ArrayList<>();
        for (GroupAd ad : ads) {
            DataRequestGroupAds data = new DataRequestGroupAds();
            data.setGroupId(String.valueOf(ad.getId()));
            dataRequestGroupAdses.add(data);
        }
        dataRequestGroupAd.setAds(dataRequestGroupAdses);
        adsActionRequest.setData(dataRequestGroupAd);
        return adsActionRequest;
    }
}
