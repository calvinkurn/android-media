package com.tokopedia.ride.common.ride.domain.model;

/**
 * Created by alvarisi on 7/17/17.
 */

public class TimePriceEstimate {
    private String productId;
    private String estimateFmt;
    private int lowEstimate;
    private int highEstimate;
    private int estimateTime;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getEstimateFmt() {
        return estimateFmt;
    }

    public void setEstimateFmt(String estimateFmt) {
        this.estimateFmt = estimateFmt;
    }

    public int getLowEstimate() {
        return lowEstimate;
    }

    public void setLowEstimate(int lowEstimate) {
        this.lowEstimate = lowEstimate;
    }

    public int getHighEstimate() {
        return highEstimate;
    }

    public void setHighEstimate(int highEstimate) {
        this.highEstimate = highEstimate;
    }

    public int getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(int estimateTime) {
        this.estimateTime = estimateTime;
    }
}
