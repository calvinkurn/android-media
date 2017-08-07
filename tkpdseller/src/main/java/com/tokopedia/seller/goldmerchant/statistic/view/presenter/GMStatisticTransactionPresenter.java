package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.listener.GMStatisticTransactionView;

/**
 * Created by normansyahputa on 7/18/17.
 */

public abstract class GMStatisticTransactionPresenter extends BaseDaggerPresenter<GMStatisticTransactionView> {

    public abstract void loadDataWithDate(long startDate, long endDate);

}