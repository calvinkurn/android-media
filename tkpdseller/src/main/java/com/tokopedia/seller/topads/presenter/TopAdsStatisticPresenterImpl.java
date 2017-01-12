package com.tokopedia.seller.topads.presenter;

import android.content.Context;

import com.tokopedia.seller.topads.view.fragment.TopAdsStatisticFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsStatisticViewListener;

/**
 * Created by zulfikarrahman on 1/6/17.
 */
public class TopAdsStatisticPresenterImpl extends TopAdsDatePickerPresenterImpl implements TopAdsStatisticPresenter {

    private final TopAdsStatisticViewListener topAdsStatisticViewListener;

    public TopAdsStatisticPresenterImpl(TopAdsStatisticViewListener topAdsStatisticViewListener, Context context) {
        super(context);
        this.topAdsStatisticViewListener = topAdsStatisticViewListener;
    }
}
