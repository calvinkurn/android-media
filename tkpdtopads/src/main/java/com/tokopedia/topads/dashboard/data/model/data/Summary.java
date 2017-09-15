package com.tokopedia.topads.dashboard.data.model.data;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class Summary {

    private long Id;
    private int clickSum;
    private String clickSumFmt;
    private double costSum;
    private String costSumFmt;
    private int impressionSum;
    private String impressionSumFmt;
    private double ctrPercentage;
    private String ctrPercentageFmt;
    private int conversionSum;
    private String conversionSumFmt;
    private double costAvg;
    private String costAvgFmt;
    private String shopId;
    private int type;
    private String startDate;
    private String endDate;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public int getClickSum() {
        return clickSum;
    }

    public void setClickSum(int clickSum) {
        this.clickSum = clickSum;
    }

    public String getClickSumFmt() {
        return clickSumFmt;
    }

    public void setClickSumFmt(String clickSumFmt) {
        this.clickSumFmt = clickSumFmt;
    }

    public double getCostSum() {
        return costSum;
    }

    public void setCostSum(double costSum) {
        this.costSum = costSum;
    }

    public String getCostSumFmt() {
        return costSumFmt;
    }

    public void setCostSumFmt(String costSumFmt) {
        this.costSumFmt = costSumFmt;
    }

    public int getImpressionSum() {
        return impressionSum;
    }

    public void setImpressionSum(int impressionSum) {
        this.impressionSum = impressionSum;
    }

    public String getImpressionSumFmt() {
        return impressionSumFmt;
    }

    public void setImpressionSumFmt(String impressionSumFmt) {
        this.impressionSumFmt = impressionSumFmt;
    }

    public double getCtrPercentage() {
        return ctrPercentage;
    }

    public void setCtrPercentage(double ctrPercentage) {
        this.ctrPercentage = ctrPercentage;
    }

    public String getCtrPercentageFmt() {
        return ctrPercentageFmt;
    }

    public void setCtrPercentageFmt(String ctrPercentageFmt) {
        this.ctrPercentageFmt = ctrPercentageFmt;
    }

    public int getConversionSum() {
        return conversionSum;
    }

    public void setConversionSum(int conversionSum) {
        this.conversionSum = conversionSum;
    }

    public String getConversionSumFmt() {
        return conversionSumFmt;
    }

    public void setConversionSumFmt(String conversionSumFmt) {
        this.conversionSumFmt = conversionSumFmt;
    }

    public double getCostAvg() {
        return costAvg;
    }

    public void setCostAvg(double costAvg) {
        this.costAvg = costAvg;
    }

    public String getCostAvgFmt() {
        return costAvgFmt;
    }

    public void setCostAvgFmt(String costAvgFmt) {
        this.costAvgFmt = costAvgFmt;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}