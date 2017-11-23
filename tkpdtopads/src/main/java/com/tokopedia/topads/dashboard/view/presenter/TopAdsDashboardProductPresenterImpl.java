package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.domain.interactor.ListenerInteractor;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.data.model.request.ShopRequest;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardFragmentListener;
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardProductFragmentListener;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDashboardProductPresenterImpl extends TopAdsDashboardPresenterImpl implements TopAdsDashboardProductPresenter {

    private static final int TYPE_PRODUCT = 1;

    private TopAdsDashboardProductFragmentListener topAdsDashboardFragmentListener;

    public void setTopAdsDashboardFragmentListener(TopAdsDashboardProductFragmentListener topAdsDashboardFragmentListener) {
        this.topAdsDashboardFragmentListener = topAdsDashboardFragmentListener;
    }

    @Override
    public int getType() {
        return TYPE_PRODUCT;
    }

    @Override
    public TopAdsDashboardFragmentListener getDashboardListener() {
        return topAdsDashboardFragmentListener;
    }

    public TopAdsDashboardProductPresenterImpl(Context context) {
        super(context);
    }

    @Override
    public void populateTotalAd() {
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setShopId(getShopId());
        dashboardTopadsInteractor.getTotalAd(shopRequest, new ListenerInteractor<TotalAd>() {
            @Override
            public void onSuccess(TotalAd totalAd) {
                if (topAdsDashboardFragmentListener != null) {
                    topAdsDashboardFragmentListener.onTotalAdLoaded(totalAd);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (topAdsDashboardFragmentListener != null) {
                    topAdsDashboardFragmentListener.onLoadTotalAdError(throwable);
                }
            }
        });
    }
}