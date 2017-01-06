package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.interactor.ListenerInteractor;
import com.tokopedia.seller.topads.interactor.TopAdsProductAdInteractor;
import com.tokopedia.seller.topads.model.data.Cell;
import com.tokopedia.seller.topads.model.request.StatisticRequest;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticActivityViewListener;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public class TopAdsStatisticActivityPresenterImpl implements TopAdsStatisticActivityPresenter {
    private final TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener;
    private final TopAdsProductAdInteractor topAdsProductAdInteractor;

    public TopAdsStatisticActivityPresenterImpl(TopAdsStatisticActivityViewListener topAdsStatisticActivityViewListener, TopAdsProductAdInteractor topAdsProductAdInteractor) {
        this.topAdsStatisticActivityViewListener = topAdsStatisticActivityViewListener;
        this.topAdsProductAdInteractor = topAdsProductAdInteractor;
    }

    @Override
    public void getStatisticFromNet(StatisticRequest statisticRequest) {
        topAdsProductAdInteractor.getStatistic(statisticRequest, new ListenerInteractor<List<Cell>>() {
            @Override
            public void onSuccess(List<Cell> cells) {
                if(cells != null) {
                    topAdsStatisticActivityViewListener.updateDataCell(cells);
                }else{
                    topAdsStatisticActivityViewListener.onError(new NullPointerException());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                topAdsStatisticActivityViewListener.onError(throwable);
            }
        });
    }
}
