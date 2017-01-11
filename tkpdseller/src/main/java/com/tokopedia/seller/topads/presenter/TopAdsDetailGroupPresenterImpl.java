package com.tokopedia.seller.topads.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsGroupAdInteractor;
import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.model.data.GroupAdAction;
import com.tokopedia.seller.topads.model.data.GroupAdBulkAction;
import com.tokopedia.seller.topads.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.model.request.DataRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/3/17.
 */
public class TopAdsDetailGroupPresenterImpl extends TopAdsDetailPresenterImpl implements TopAdsDetailGroupPresenter {

    private final TopAdsGroupAdInteractor topAdsGroupAdInteractor;

    public TopAdsDetailGroupPresenterImpl(Context context, TopAdsDetailViewListener topAdsDetailViewListener, TopAdsGroupAdInteractor topAdsGroupAdInteractor) {
        super(context, topAdsDetailViewListener);
        this.topAdsGroupAdInteractor = topAdsGroupAdInteractor;
    }

    @Override
    public void turnOnAds(Ad ad, String shopId) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ad, TopAdsNetworkConstant.ACTION_BULK_ON_AD, shopId);
        actionBulkAds(dataRequest);
    }

    @Override
    public void turnOffAds(Ad ad, String shopId) {
        DataRequest<GroupAdBulkAction> dataRequest = generateActionRequest(ad, TopAdsNetworkConstant.ACTION_BULK_OFF_AD, shopId);
        actionBulkAds(dataRequest);
    }

    @NonNull
    private void actionBulkAds(DataRequest<GroupAdBulkAction> actionRequest) {
        topAdsDetailViewListener.showProgress();
        topAdsGroupAdInteractor.bulkAction(actionRequest, new ListenerInteractor<GroupAdBulkAction>() {
            @Override
            public void onSuccess(GroupAdBulkAction dataResponseActionAds) {
                topAdsDetailViewListener.dismissProgress();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsDetailViewListener.dismissProgress();
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
}
