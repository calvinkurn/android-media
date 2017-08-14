package com.tokopedia.seller.goldmerchant.statistic.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;
import com.tokopedia.seller.topads.dashboard.data.model.data.DataDeposit;

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
