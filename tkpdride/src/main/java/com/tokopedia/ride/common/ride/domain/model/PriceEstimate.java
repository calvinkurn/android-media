package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 7/17/17.
 */

public class PriceEstimate {
    private String currencyCode;
    private double distance;
    private String estimate;
    private int highEstimate;
    private int lowEstimate;
    private int duration;
    private String productId;

    public PriceEstimate() {
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public int getHighEstimate() {
        return highEstimate;
    }

    public void setHighEstimate(int highEstimate) {
        this.highEstimate = highEstimate;
    }

    public int getLowEstimate() {
        return lowEstimate;
    }

    public void setLowEstimate(int lowEstimate) {
        this.lowEstimate = lowEstimate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
