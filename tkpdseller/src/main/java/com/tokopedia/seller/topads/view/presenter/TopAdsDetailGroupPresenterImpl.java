package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.domain.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.seller.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.seller.topads.dashboard.data.model.data.GroupAdAction;
import com.tokopedia.seller.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.seller.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.dashboard.data.model.response.PageDataResponse;
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
    public void refreshAd(Date startDate, Date endDate, String id) {
        SearchAdRequest searchAdRequest = new SearchAdRequest();
        searchAdRequest.setShopId(getShopId());
        searchAdRequest.setStartDate(startDate);
        searchAdRequest.setEndDate(endDate);
        searchAdRequest.setGroupId(id);
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
    public void turnOnAds(String id) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
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
    public void turnOffAds(String id) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
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

    @Override
    public void deleteAd(String id) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_DELETE_AD);
        groupAdInteractor.bulkAction(dataRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onDeleteAdSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onDeleteAdError();
            }
        });
    }

    @NonNull
    private DataRequest<GroupAdBulkAction> generateActionRequest(String id, String action) {
        DataRequest<GroupAdBulkAction> dataRequest = new DataRequest<>();
        GroupAdBulkAction dataRequestGroupAd = new GroupAdBulkAction();
        dataRequestGroupAd.setAction(action);
        dataRequestGroupAd.setShopId(getShopId());
        List<GroupAdAction> dataRequestGroupAdses = new ArrayList<>();
        GroupAdAction adAction = new GroupAdAction();
        adAction.setId(id);
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
