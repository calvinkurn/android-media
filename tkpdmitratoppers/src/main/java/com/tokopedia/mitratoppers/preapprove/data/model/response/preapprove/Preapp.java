package com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 23/01/18.
 */

public class Preapp {

    @SerializedName("valid")
    @Expose
    private String valid; // "2017-11-26"
    @SerializedName("3m")
    @Expose
    private RatePerMonth ratePerMonth3m;
    @SerializedName("6m")
    @Expose
    private RatePerMonth ratePerMonth6m;
    @SerializedName("12m")
    @Expose
    private RatePerMonth ratePerMonth12m;

    public String getValid() {
        return valid;
    }

    public RatePerMonth getRatePerMonth3m() {
        return ratePerMonth3m;
    }

    public RatePerMonth getRatePerMonth6m() {
        return ratePerMonth6m;
    }

    public RatePerMonth getRatePerMonth12m() {
        return ratePerMonth12m;
    }

}