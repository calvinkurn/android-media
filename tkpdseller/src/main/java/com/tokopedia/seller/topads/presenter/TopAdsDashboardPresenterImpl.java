package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.request.ShopRequest;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;

import java.util.Date;

/**
 * Created by Nisie on 5/9/16.
 */
public abstract class TopAdsDashboardPresenterImpl extends TopAdsDatePickerPresenterImpl implements TopAdsDashboardPresenter {

    protected DashboardTopadsInteractor dashboardTopadsInteractor;
    private Context context;

    public abstract int getType();

    public abstract TopAdsDashboardFragmentListener getDashboardListener();

    public TopAdsDashboardPresenterImpl(Context context) {
        super(context);
        this.context = context;
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    protected String getShopId() {
        SessionHandler session = new SessionHandler(context);
        return session.getShopID();
    }

    public void populateSummary(final Date startDate, final Date endDate) {
        StatisticRequest statisticRequest = new StatisticRequest();
        statisticRequest.setShopId(getShopId());
        statisticRequest.setType(getType());
        statisticRequest.setStartDate(startDate);
        statisticRequest.setEndDate(endDate);
        dashboardTopadsInteractor.getDashboardSummary(statisticRequest, new ListenerInteractor<Summary>() {
            @Override
            public void onSuccess(Summary summary) {
                if (getDashboardListener() != null) {
                    getDashboardListener().onSummaryLoaded(summary);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (getDashboardListener() != null) {
                    getDashboardListener().onLoadSummaryError(throwable);
                }
            }
        });
    }

    @Override
    public void populateDeposit() {
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setShopId(getShopId());
        dashboardTopadsInteractor.getDeposit(shopRequest, new ListenerInteractor<DataDeposit>() {
            @Override
            public void onSuccess(DataDeposit dataDeposit) {
                if (getDashboardListener() != null) {
                    getDashboardListener().onDepositTopAdsLoaded(dataDeposit);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (getDashboardListener() != null) {
                    getDashboardListener().onLoadDepositTopAdsError(throwable);
                }
            }
        });
    }

    @Override
    public void populateShopInfo() {
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setShopId(getShopId());
        dashboardTopadsInteractor.getShopInfo(shopRequest, new ListenerInteractor<ShopModel>() {
            @Override
            public void onSuccess(ShopModel shopModel) {
                if (getDashboardListener() != null) {
                    getDashboardListener().onShopDetailLoaded(shopModel);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (getDashboardListener() != null) {
                    getDashboardListener().onLoadShopDetailError(throwable);
                }
            }
        });
    }

    @Override
    public void unSubscribe() {
        if (dashboardTopadsInteractor != null) {
            dashboardTopadsInteractor.unSubscribe();
        }
    }
}