package com.tokopedia.tkpdpdp.estimasiongkir.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tkpdpdp.estimasiongkir.RatesModel;

public interface RatesEstimationDetailView extends CustomerView {
    void onErrorLoadRateEstimaion(Throwable throwable);

    void onSuccesLoadRateEstimaion(RatesModel ratesModel);
}
