package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 3/21/17.
 */

public class Fare {
    String fareId;
    int expiresAt;
    String currencyCode;
    float value;
    String display;

    public Fare() {
    }

    public String getFareId() {
        return fareId;
    }

    public void setFareId(String fareId) {
        this.fareId = fareId;
    }

    public int getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(int expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
