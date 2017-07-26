package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.listener.GMStatisticDashboardView;

/**
 * Created by normansyahputa on 1/2/17.
 */

public abstract class GMDashboardPresenter extends BaseDaggerPresenter<GMStatisticDashboardView> {

    public abstract void fetchData(long startDate, long endDate);
}
