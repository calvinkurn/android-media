package com.tokopedia.topads.dashboard.view.presenter;

import android.content.Context;

import com.tokopedia.topads.dashboard.view.listener.TopAdsStatisticViewListener;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public class TopAdsStatisticPresenterImpl implements TopAdsStatisticPresenter {

    private final TopAdsStatisticViewListener topAdsStatisticViewListener;

    public TopAdsStatisticPresenterImpl(TopAdsStatisticViewListener topAdsStatisticViewListener, Context context) {
        this.topAdsStatisticViewListener = topAdsStatisticViewListener;
    }
}