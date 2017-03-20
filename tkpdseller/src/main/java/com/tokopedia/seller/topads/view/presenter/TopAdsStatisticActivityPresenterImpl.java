package com.tokopedia.seller.topads.view.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.domain.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductAdInteractor;
import com.tokopedia.seller.topads.data.model.data.Cell;
import com.tokopedia.seller.topads.data.model.request.StatisticRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticActivityViewListener;

import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public class TopAdsStatisticActivityPresenterImpl implements TopAdsStatisticActivityPresenter {
    private final TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener;
    private final TopAdsProductAdInteractor topAdsProductAdInteractor;
    StatisticRequest statisticRequest = new StatisticRequest();

    public TopAdsStatisticActivityPresenterImpl(TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener,
                                                TopAdsProductAdInteractor topAdsProductAdInteractor, Context context) {
        this.topAdsStatisticActivityViewListener = topAdsStatisticActivityViewListener;
        this.topAdsProductAdInteractor = topAdsProductAdInteractor;
    }

    @Override
    public void getStatisticFromNet(Date startDate, Date endDate, int typeRequest, String shopId) {
        topAdsStatisticActivityViewListener.showLoading();
        statisticRequest.setStartDate(startDate);
        statisticRequest.setEndDate(endDate);
        statisticRequest.setShopId(shopId);
        statisticRequest.setType(typeRequest);
        topAdsProductAdInteractor.getStatistic(statisticRequest, new ListenerInteractor<List<Cell>>() {
            @Override
            public void onSuccess(List<Cell> cells) {
                if (cells != null) {
                    topAdsStatisticActivityViewListener.updateDataCell(cells);
                } else {
                    topAdsStatisticActivityViewListener.onError(new NullPointerException());
                }
                topAdsStatisticActivityViewListener.dismissLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsStatisticActivityViewListener.dismissLoading();
                topAdsStatisticActivityViewListener.onError(throwable);
            }
        });
    }
}
