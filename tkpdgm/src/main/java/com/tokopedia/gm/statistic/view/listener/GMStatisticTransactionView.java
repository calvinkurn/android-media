package com.tokopedia.gm.statistic.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.gm.statistic.view.model.GMGraphViewModel;
import com.tokopedia.gm.statistic.view.model.GMTransactionGraphMergeModel;

/**
 * Created by normansyahputa on 7/18/17.
 */

public interface GMStatisticTransactionView extends CustomerView {
    void onSuccessLoadTransactionGraph(GMTransactionGraphMergeModel mergeModel);

    void onErrorLoadTransactionGraph(Throwable t);

    void bindTopAds(GMGraphViewModel gmTopAdsAmountViewModel);

    void bindTopAdsCreditNotUsed(GMGraphViewModel gmTopAdsAmountViewModel, DataDeposit dataDeposit);

    void onErrorLoadTopAdsGraph(Throwable t);
}
