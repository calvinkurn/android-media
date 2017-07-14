package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;

import java.util.Date;

/**
 * Created by normansyahputa on 7/13/17.
 */

public abstract class GMStatisticTransactionTablePresenter extends BaseDaggerPresenter<GMStatisticTransactionTableView> {
    public abstract void loadData(Date startDate, Date endDate);
}
