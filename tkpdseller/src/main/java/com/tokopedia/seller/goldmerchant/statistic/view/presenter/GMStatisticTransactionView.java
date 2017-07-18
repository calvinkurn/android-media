package com.tokopedia.seller.goldmerchant.statistic.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

/**
 * Created by normansyahputa on 7/18/17.
 */

public interface GMStatisticTransactionView extends CustomerView {
    void revealData(GMTransactionGraphMergeModel mergeModel);
}
