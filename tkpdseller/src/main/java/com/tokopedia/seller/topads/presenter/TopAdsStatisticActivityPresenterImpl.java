package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractor;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticActivityViewListener;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public class TopAdsStatisticActivityPresenterImpl extends TopAdsDatePickerPresenterImpl implements TopAdsStatisticActivityPresenter {
    private final TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener;
    private final TopAdsProductAdInteractor topAdsProductAdInteractor;
    StatisticRequest statisticRequest = new StatisticRequest();

    public TopAdsStatisticActivityPresenterImpl(TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener,
                                                TopAdsProductAdInteractor topAdsProductAdInteractor, Context context) {
        super(context);
        this.topAdsStatisticActivityViewListener = topAdsStatisticActivityViewListener;
        this.topAdsProductAdInteractor = topAdsProductAdInteractor;
    }

    @Override
    public void getStatisticFromNet(int typeRequest, String shopId) {
        topAdsStatisticActivityViewListener.showLoading();
        statisticRequest.setEndDate(getEndDate());
        statisticRequest.setStartDate(getStartDate());
        statisticRequest.setShopId(shopId);
        statisticRequest.setType(typeRequest);
        topAdsProductAdInteractor.getStatistic(statisticRequest, new ListenerInteractor<List<Cell>>() {
            @Override
            public void onSuccess(List<Cell> cells) {
                if(cells != null) {
                    topAdsStatisticActivityViewListener.updateDataCell(cells);
                }else{
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

    @Override
    public boolean isDateUpdated() {
        return isDateUpdated(statisticRequest.getStartDate(), statisticRequest.getEndDate());
    }
}
