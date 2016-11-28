package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.exchange.DepositRequest;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsProductFragmentListener;

import java.util.Date;

/**
 * Created by Nisie on 5/9/16.
 */
public class TopAdsProductFragmentPresenterImpl implements TopAdsProductFragmentPresenter {

    private static final int TYPE_PRODUCT = 1;

    private DashboardTopadsInteractor dashboardTopadsInteractor;
    private Context context;

    private TopAdsProductFragmentListener topAdsProductFragmentListener;

    public void setTopAdsProductFragmentListener(TopAdsProductFragmentListener topAdsProductFragmentListener) {
        this.topAdsProductFragmentListener = topAdsProductFragmentListener;
    }

    public TopAdsProductFragmentPresenterImpl(Context context) {
        this.context = context;
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl(context);
    }

    public int getType() {
        return TYPE_PRODUCT;
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
                if (topAdsProductFragmentListener != null) {
                    topAdsProductFragmentListener.onSummaryLoaded(summary);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (topAdsProductFragmentListener != null) {
                    topAdsProductFragmentListener.onLoadSummaryError(throwable);
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
                if (topAdsProductFragmentListener != null) {
                    topAdsProductFragmentListener.onDepositTopAdsLoaded(dataDeposit);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (topAdsProductFragmentListener != null) {
                    topAdsProductFragmentListener.onLoadDepositTopAdsError(throwable);
                }
            }
        });
    }
}