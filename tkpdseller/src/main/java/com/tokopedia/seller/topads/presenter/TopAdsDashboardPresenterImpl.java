package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.exchange.DepositRequest;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;

import java.util.Date;

/**
 * Created by Nisie on 5/9/16.
 */
public abstract class TopAdsDashboardPresenterImpl implements TopAdsDashboardPresenter {

    private DashboardTopadsInteractor dashboardTopadsInteractor;
    private Context context;

    private TopAdsDashboardFragmentListener mTopAdsDashboardFragmentListener;

    public void setTopAdsDashboardFragmentListener(TopAdsDashboardFragmentListener topAdsDashboardFragmentListener) {
        this.mTopAdsDashboardFragmentListener = topAdsDashboardFragmentListener;
    }

    public abstract int getType();

    public TopAdsDashboardPresenterImpl(Context context) {
        this.context = context;
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    private String getShopId() {
        SessionHandler session = new SessionHandler(context);
        return session.getShopID();
    }

    public void populateSummary(final Date startDate, final Date endDate) {
        StatisticRequest statisticRequest = new StatisticRequest();
        statisticRequest.setShopId(getShopId());
        statisticRequest.setType(getType());
        statisticRequest.setStartDate(startDate);
        statisticRequest.setEndDate(endDate);
        dashboardTopadsInteractor.getDashboardSummary(statisticRequest, new DashboardTopadsInteractor.Listener<Summary>() {
            @Override
            public void onSuccess(Summary summary) {
                if (mTopAdsDashboardFragmentListener != null) {
                    mTopAdsDashboardFragmentListener.onSummaryLoaded(summary);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (mTopAdsDashboardFragmentListener != null) {
                    mTopAdsDashboardFragmentListener.onLoadSummaryError(throwable);
                }
            }
        });
    }

    @Override
    public void populateDeposit() {
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setShopId(getShopId());
        dashboardTopadsInteractor.getDeposit(depositRequest, new DashboardTopadsInteractor.Listener<DataDeposit>() {
            @Override
            public void onSuccess(DataDeposit dataDeposit) {
                if (mTopAdsDashboardFragmentListener != null) {
                    mTopAdsDashboardFragmentListener.onDepositTopAdsLoaded(dataDeposit);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (mTopAdsDashboardFragmentListener != null) {
                    mTopAdsDashboardFragmentListener.onLoadDepositTopAdsError(throwable);
                }
            }
        });
    }

    @Override
    public void populateShopInfo() {
        dashboardTopadsInteractor.getShopInfo(getShopId(), new DashboardTopadsInteractor.Listener<ShopModel>() {
            @Override
            public void onSuccess(ShopModel shopModel) {
                if (mTopAdsDashboardFragmentListener != null) {
                    mTopAdsDashboardFragmentListener.onShopDetailLoaded(shopModel);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (mTopAdsDashboardFragmentListener != null) {
                    mTopAdsDashboardFragmentListener.onLoadShopDetailError(throwable);
                }
            }
        });
    }
}