package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailListener;

import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 8/14/17.
 */

public class TopAdsDetailGroupPresenterImpl extends TopAdsDetailPresenterImpl<GroupAd> implements TopAdsDetailPresenter {

    protected final TopAdsGroupAdInteractor groupAdInteractor;

    public TopAdsDetailGroupPresenterImpl(Context context, TopAdsDetailListener<GroupAd> topAdsDetailListener, TopAdsGroupAdInteractor groupAdInteractor) {
        super(context, topAdsDetailListener);
        this.groupAdInteractor = groupAdInteractor;
    }

    @Override
    public void unSubscribe() {
        if (groupAdInteractor != null) {
            groupAdInteractor.unSubscribe();
        }
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, String id) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setGroupId(id);
        groupAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<GroupAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<GroupAd>> pageDataResponse) {
                topAdsDetailListener.onAdLoaded(pageDataResponse.getData().get(0));
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailListener.onLoadAdError();
            }
        });
    }
}
