package com.tokopedia.tkpdpdp.estimasiongkir;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatesEstimationModel {
    @SerializedName("rates")
    @Expose
    private RatesModel rates;

    public RatesModel getRates() {
        return rates;
    }

    public class Response {
        @SerializedName("rates_estimate")
        @Expose
        private RatesEstimationModel ratesEstimation;

        public RatesEstimationModel getRatesEstimation() {
            return ratesEstimation;
        }
    }
}
