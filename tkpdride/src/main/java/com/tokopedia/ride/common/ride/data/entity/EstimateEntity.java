package com.tokopedia.ride.common.ride.data.entity;

/**
 * Created by vishal.gupta on 4/3/17.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EstimateEntity {

    @SerializedName("surge_confirmation_href")
    @Expose
    private String surgeConfirmationHref;
    @SerializedName("high_estimate")
    @Expose
    private Integer highEstimate;
    @SerializedName("surge_confirmation_id")
    @Expose
    private String surgeConfirmationId;
    @SerializedName("minimum")
    @Expose
    private Integer minimum;
    @SerializedName("low_estimate")
    @Expose
    private Integer lowEstimate;
    @SerializedName("fare_breakdown")
    @Expose
    private List<FareBreakdownEntity> fareBreakdown = null;
    @SerializedName("surge_multiplier")
    @Expose
    private Double surgeMultiplier;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    public String getSurgeConfirmationHref() {
        return surgeConfirmationHref;
    }

    public void setSurgeConfirmationHref(String surgeConfirmationHref) {
        this.surgeConfirmationHref = surgeConfirmationHref;
    }

    public Integer getHighEstimate() {
        return highEstimate;
    }

    public void setHighEstimate(Integer highEstimate) {
        this.highEstimate = highEstimate;
    }

    public String getSurgeConfirmationId() {
        return surgeConfirmationId;
    }

    public void setSurgeConfirmationId(String surgeConfirmationId) {
        this.surgeConfirmationId = surgeConfirmationId;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getLowEstimate() {
        return lowEstimate;
    }

    public void setLowEstimate(Integer lowEstimate) {
        this.lowEstimate = lowEstimate;
    }

    public List<FareBreakdownEntity> getFareBreakdown() {
        return fareBreakdown;
    }

    public void setFareBreakdown(List<FareBreakdownEntity> fareBreakdown) {
        this.fareBreakdown = fareBreakdown;
    }

    public Double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(Double surgeMultiplier) {
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