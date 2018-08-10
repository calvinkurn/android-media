package com.tokopedia.tkpdpdp.estimasiongkir.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatesEstimationModel (
    @SerializedName("rates")
    @Expose
    val rates: RatesModel = RatesModel()){

    data class Response (
        @SerializedName("rates_estimate")
        @Expose
        val ratesEstimation: RatesEstimationModel = RatesEstimationModel()
    )
}
