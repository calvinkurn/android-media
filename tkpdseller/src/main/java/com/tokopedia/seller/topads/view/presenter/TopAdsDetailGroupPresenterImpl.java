package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.domain.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.GroupAdAction;
import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.response.PageDataResponse;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/3/17.
 */
public class TopAdsDetailGroupPresenterImpl extends TopAdsDetailPresenterImpl implements TopAdsDetailGroupPresenter {

    private final TopAdsGroupAdInteractor groupAdInteractor;

    public TopAdsDetailGroupPresenterImpl(Context context, TopAdsDetailViewListener topAdsDetailViewListener, TopAdsGroupAdInteractor groupAdInteractor) {
        super(context, topAdsDetailViewListener);
        this.groupAdInteractor = groupAdInteractor;
    }

    @Override
    public void refreshAd(Date startDate, Date endDate, int id) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setGroupId(String.valueOf(id));
        groupAdInteractor.searchAd(searchAdRequest, new ListenerInteractor<PageDataResponse<List<GroupAd>>>() {
            @Override
            public void onSuccess(PageDataResponse<List<GroupAd>> pageDataResponse) {
                topAdsDetailViewListener.onAdLoaded(pageDataResponse.getData().get(0));
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onLoadAdError();
            }
        });
    }

    @Override
    public void turnOnAds(Ad ad, String shopId) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ad, TopAdsNetworkConstant.ACTION_BULK_ON_AD, shopId);
        groupAdInteractor.bulkAction(dataRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOnAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onTurnOnAdError();
            }
        });
    }

    @Override
    public void turnOffAds(Ad ad, String shopId) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ad, TopAdsNetworkConstant.ACTION_BULK_OFF_AD, shopId);
        groupAdInteractor.bulkAction(dataRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOffAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onTurnOffAdError();
            }
        });
    }

    @NonNull
    private DataRequest<GroupAdBulkAction> generateActionRequest(Ad ad, String action, String shopId) {
        DataRequest<GroupAdBulkAction> dataRequest = new DataRequest<>();
        GroupAdBulkAction dataRequestGroupAd = new GroupAdBulkAction();
        dataRequestGroupAd.setAction(action);
        dataRequestGroupAd.setShopId(shopId);
        List<GroupAdAction> dataRequestGroupAdses = new ArrayList<>();
        GroupAdAction adAction = new GroupAdAction();
        adAction.setId(String.valueOf(ad.getId()));
        dataRequestGroupAdses.add(adAction);
        dataRequestGroupAd.setAdList(dataRequestGroupAdses);
        dataRequest.setData(dataRequestGroupAd);
        return dataRequest;
    }

    @Override
    public void unSubscribe() {
        if (groupAdInteractor != null) {
            groupAdInteractor.unSubscribe();
        }
    }
}
