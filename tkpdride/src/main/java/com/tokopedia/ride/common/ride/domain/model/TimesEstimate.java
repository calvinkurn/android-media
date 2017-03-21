package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 3/20/17.
 */

public class TimesEstimate {
    private String displayName;
    private String estimate;
    private String localizedDisplayName;
    private String productId;

    public TimesEstimate() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getLocalizedDisplayName() {
        return localizedDisplayName;
    }

    public void setLocalizedDisplayName(String localizedDisplayName) {
        this.localizedDisplayName = localizedDisplayName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
