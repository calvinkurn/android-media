package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsShopAdInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsShopAdInteractorImpl;
import com.tokopedia.seller.topads.model.data.ShopAd;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.request.ShopRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardStoreFragmentListener;

import java.util.Date;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsDashboardShopPresenterImpl extends TopAdsDashboardPresenterImpl implements TopAdsDashboardShopPresenter {

    private static final int TYPE_SHOP = 2;

    private TopAdsShopAdInteractor topAdsShopAdInteractor;
    private TopAdsDashboardStoreFragmentListener topAdsDashboardFragmentListener;

    public void setTopAdsDashboardFragmentListener(TopAdsDashboardStoreFragmentListener topAdsDashboardFragmentListener) {
        this.topAdsDashboardFragmentListener = topAdsDashboardFragmentListener;
    }

    @Override
    public TopAdsDashboardFragmentListener getDashboardListener() {
        return topAdsDashboardFragmentListener;
    }

    @Override
    public int getType() {
        return TYPE_SHOP;
    }

    public TopAdsDashboardShopPresenterImpl(Context context) {
        super(context);
        topAdsShopAdInteractor = new TopAdsShopAdInteractorImpl(context);
    }

    @Override
    public void populateShopAd(Date startDate, Date endDate) {
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setShopId(getShopId());
        SearchAdRequest request = new SearchAdRequest();
        request.setShopId(getShopId());
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        topAdsShopAdInteractor.getShopAd(request, new ListenerInteractor<ShopAd>() {
            @Override
            public void onSuccess(ShopAd shopAd) {
                if (topAdsDashboardFragmentListener != null) {
                    topAdsDashboardFragmentListener.onAdShopLoaded(shopAd);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (topAdsDashboardFragmentListener != null) {
                    topAdsDashboardFragmentListener.onLoadAdShopError();;
                }
            }
        });
    }
}