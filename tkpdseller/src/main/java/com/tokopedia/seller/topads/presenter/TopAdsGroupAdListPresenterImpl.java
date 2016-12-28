package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.GroupAdAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListPresenterImpl extends TopAdsAdListPresenterImpl<GroupAd> implements TopAdsGroupAdListPresenter {

    public TopAdsGroupAdListPresenterImpl(Context context, TopAdsListPromoViewListener topAdsListPromoViewListener) {
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
            public void onSuccess(List<GroupAd> adList) {
                topAdsListPromoViewListener.onSearchAdLoaded(adList);
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsListPromoViewListener.onLoadSearchAdError();
            }
        });
    }

    @Override
    public void actionDeleteAds(List<GroupAd> ads) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        actionBulkAds(dataRequest);
    }

    @Override
    public void actionOffAds(List<GroupAd> ads) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        actionBulkAds(dataRequest);
    }

    @Override
    public void actionOnAds(List<GroupAd> ads) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ads, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        actionBulkAds(dataRequest);
    }

    @NonNull
    private void actionBulkAds(DataRequest<GroupAdBulkAction> actionRequest) {
        dashboardTopadsInteractor.actionGroupAds(actionRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @NonNull
    private DataRequest<GroupAdBulkAction> generateActionRequest(List<GroupAd> ads, String action) {
        DataRequest<GroupAdBulkAction> dataRequest = new DataRequest<>();
        GroupAdBulkAction dataRequestGroupAd = new GroupAdBulkAction();
        dataRequestGroupAd.setAction(action);
        dataRequestGroupAd.setShopId(SessionHandler.getShopID(context));
        List<GroupAdAction> dataRequestGroupAdses = new ArrayList<>();
        for (GroupAd ad : ads) {
            GroupAdAction data = new GroupAdAction();
            data.setId(ad.getId());
            dataRequestGroupAdses.add(data);
        }
        dataRequestGroupAd.setAdList(dataRequestGroupAdses);
        dataRequest.setData(dataRequestGroupAd);
        return dataRequest;
    }
}
