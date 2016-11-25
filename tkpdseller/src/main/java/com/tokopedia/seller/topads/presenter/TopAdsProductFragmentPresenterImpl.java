package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.exchange.StatisticRequest;
import com.tokopedia.seller.topads.model.exchange.StatisticResponse;
import com.tokopedia.seller.topads.view.listener.TopAdsProductFragmentListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
        dashboardTopadsInteractor = new DashboardTopadsInteractorImpl();
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
}