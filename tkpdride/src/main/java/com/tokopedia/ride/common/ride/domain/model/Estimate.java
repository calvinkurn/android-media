package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by vishal.gupta on 4/27/17.
 */

public class Estimate {

    private String surgeConfirmationHref;
    private float highEstimate;
    private String surgeConfirmationId;
    private float lowEstimate;
    private float surgeMultiplier;
    private String display;
    private String currencyCode;

    public String getSurgeConfirmationHref() {
        return surgeConfirmationHref;
    }

    public void setSurgeConfirmationHref(String surgeConfirmationHref) {
        this.surgeConfirmationHref = surgeConfirmationHref;
    }

    public float getHighEstimate() {
        return highEstimate;
    }

    public void setHighEstimate(float highEstimate) {
        this.highEstimate = highEstimate;
    }

    public String getSurgeConfirmationId() {
        return surgeConfirmationId;
    }

    public void setSurgeConfirmationId(String surgeConfirmationId) {
        this.surgeConfirmationId = surgeConfirmationId;
    }

    public float getLowEstimate() {
        return lowEstimate;
    }

    public void setLowEstimate(float lowEstimate) {
        this.lowEstimate = lowEstimate;
    }

    public float getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(float surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
