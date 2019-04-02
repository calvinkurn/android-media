package com.tokopedia.tkpdpdp.estimasiongkir.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesEstimationModel
import com.tokopedia.tkpdpdp.estimasiongkir.data.model.RatesModel

interface RatesEstimationDetailView : CustomerView {
    fun onErrorLoadRateEstimaion(throwable: Throwable)

    fun onSuccesLoadRateEstimaion(ratesEstimationModel: RatesEstimationModel)
}
