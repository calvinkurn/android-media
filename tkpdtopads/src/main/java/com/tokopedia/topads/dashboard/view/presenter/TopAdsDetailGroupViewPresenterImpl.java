package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdAction;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDetailViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/3/17.
 */
public class TopAdsDetailGroupViewPresenterImpl extends TopAdsDetailGroupPresenterImpl implements TopAdsDetailGroupPresenter {

    private TopAdsDetailViewListener<GroupAd> topAdsDetailViewListener;


    public TopAdsDetailGroupViewPresenterImpl(Context context,
                                              TopAdsDetailViewListener<GroupAd> topAdsDetailViewListener,
                                              TopAdsGroupAdInteractor groupAdInteractor) {
        super(context, topAdsDetailViewListener, groupAdInteractor, null, null);
        this.topAdsDetailViewListener = topAdsDetailViewListener;
    }

    @Override
    public void turnOnAds(String id) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_ON_AD);
        groupAdInteractor.bulkAction(dataRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOnAdSuccess(dataResponseActionAds);
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.onTurnOnAdError(throwable);
            }
        });
    }

    @Override
    public void turnOffAds(String id) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(id, TopAdsNetworkConstant.ACTION_BULK_OFF_AD);
        groupAdInteractor.bulkAction(dataRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.onTurnOffAdSuccess(dataResponseActionAds);
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


}
