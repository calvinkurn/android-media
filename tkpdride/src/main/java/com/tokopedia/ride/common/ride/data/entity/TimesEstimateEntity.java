package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 3/20/17.
 */

public class TimesEstimateEntity {
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("estimate")
    @Expose
    private int estimate;
    @SerializedName("localized_display_name")
    @Expose
    private String localizedDisplayName;
    @SerializedName("product_id")
    @Expose
    private String productId;

    public TimesEstimateEntity() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getEstimate() {
        return estimate;
    }

    public String getLocalizedDisplayName() {
        return localizedDisplayName;
    }

    public String getProductId() {
        return productId;
    }
}
