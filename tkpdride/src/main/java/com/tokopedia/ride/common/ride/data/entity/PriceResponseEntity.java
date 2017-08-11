package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 7/17/17.
 */

public class PriceResponseEntity {
    @SerializedName("prices")
    @Expose
    private List<PriceEntity> prices;

    public List<PriceEntity> getPrices() {
        return prices;
    }
}
