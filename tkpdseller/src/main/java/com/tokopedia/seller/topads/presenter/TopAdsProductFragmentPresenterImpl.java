package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.interactor.DashboardTopadsInteractorImpl;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.data.Summary;
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

    private static final int TYPE_PRODUCT = 0;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

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
        if (isDataValid(startDate, endDate)) {
            populateSummaryFromCache(startDate, endDate);
            return;
        }
        populateStatisticFromApi(startDate, endDate, new DashboardTopadsInteractor.Listener<StatisticResponse>() {
            @Override
            public void onSuccess(StatisticResponse response) {
                insertSummaryToCache(startDate, endDate, response.getData().getSummary());
                insertCellListToCache(response.getData().getCells());
                populateSummaryFromCache(startDate, endDate);
            }

            @Override
            public void onError(Throwable throwable) {
                if (topAdsProductFragmentListener != null) {
                    topAdsProductFragmentListener.onLoadSummaryError(throwable);
                }
            }
        });
    }

    private void populateStatisticFromApi(Date startDate, Date endDate, DashboardTopadsInteractor.Listener<StatisticResponse> listener) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(TopAdsNetworkConstant.PARAM_SHOP_ID, getShopId());
        hashMap.put(TopAdsNetworkConstant.PARAM_TYPE, String.valueOf(getType()));
        hashMap.put(TopAdsNetworkConstant.PARAM_START_DATE, new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(startDate));
        hashMap.put(TopAdsNetworkConstant.PARAM_END_DATE, new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(endDate));
        dashboardTopadsInteractor.getDashboardStatistic(hashMap, listener);
    }

    private void insertSummaryToCache(Date startDate, Date endDate, Summary summary){
        // TODO insert Summary to database based on start date and end date
    }

    private void insertCellListToCache(List<Cell> cellList){
        // TODO insert cell list to database
    }

    private void populateSummaryFromCache(Date startDate, Date endDate) {
        // TODO get Summary from database
        if (topAdsProductFragmentListener != null) {

        }
    }

    private boolean isDataValid(Date startDate, Date endDate) {
        return false;
    }
}