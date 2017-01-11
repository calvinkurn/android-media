package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsGroupAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.GroupAdAction;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.response.PageDataResponse;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupAdListPresenterImpl extends TopAdsAdListPresenterImpl<GroupAd> implements TopAdsGroupAdListPresenter {

    protected final TopAdsGroupAdInteractor groupAdInteractor;

    public TopAdsGroupAdListPresenterImpl(Context context, TopAdsListPromoViewListener topAdsListPromoViewListener) {
        super(context, topAdsListPromoViewListener);
        this.groupAdInteractor = new TopAdsGroupAdInteractorImpl(context);
    }

    @Override
    public void searchAd(Date startDate, Date endDate, String keyword, int status, final int page) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setKeyword(keyword);
        searchAdRequest.setStatus(status);
        searchAdRequest.setPage(page);
        groupAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<GroupAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<GroupAd>> pageDataResponse) {
                topAdsListPromoViewListener.onSearchAdLoaded(pageDataResponse.getData(), pageDataResponse.getPage().getTotal());
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsListPromoViewListener.onLoadSearchAdError();
            }
        });
    }

    @Override
    public void turnOffAdList(List<GroupAd> adList) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(adList, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        groupAdInteractor.bulkAction(dataRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {
                topAdsListPromoViewListener.onTurnOnAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsListPromoViewListener.onTurnOnAdFailed();
            }
        });
    }

    @Override
    public void turnOnAddList(List<GroupAd> adList) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(adList, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        groupAdInteractor.bulkAction(dataRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {
                topAdsListPromoViewListener.onTurnOnAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsListPromoViewListener.onTurnOnAdFailed();
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
            data.setId(String.valueOf(ad.getId()));
            dataRequestGroupAdses.add(data);
        }
        dataRequestGroupAd.setAdList(dataRequestGroupAdses);
        dataRequest.setData(dataRequestGroupAd);
        return dataRequest;
    }

    @Override
    public void onDestroy() {
        if (groupAdInteractor != null) {
            groupAdInteractor.unSubscribe();
        }
    }
}
