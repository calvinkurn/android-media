package com.tokopedia.seller.goldmerchant.statistic.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.goldmerchant.statistic.view.helper.model.GMGraphViewModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

/**
 * Created by normansyahputa on 7/18/17.
 */

public interface GMStatisticTransactionView extends CustomerView {
    void revealData(GMTransactionGraphMergeModel mergeModel);

    void bindTopAdsNoData(GMGraphViewModel gmTopAdsAmountViewModel);

    void bindTopAds(GMGraphViewModel gmTopAdsAmountViewModel);

    void bindNoTopAdsCredit(GMGraphViewModel gmTopAdsAmountViewModel);

    void bindTopAdsCreditNotUsed(GMGraphViewModel gmTopAdsAmountViewModel);

    void startTransactionProductList(long startDate, long endDate);
}
