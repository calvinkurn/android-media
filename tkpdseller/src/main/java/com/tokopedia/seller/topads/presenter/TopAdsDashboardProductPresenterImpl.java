package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.model.exchange.ShopRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardProductFragmentListener;

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
        dashboardTopadsInteractor.getTotalAd(shopRequest, new DashboardTopadsInteractor.Listener<TotalAd>() {
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