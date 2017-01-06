package com.tokopedia.seller.topads.model.data;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cell {

    @SerializedName("click_sum")
    @Expose
    private int clickSum;
    @SerializedName("date.month")
    @Expose
    private int dateMonth;
    @SerializedName("cost_sum")
    @Expose
    private double costSum;
    @SerializedName("impression_sum")
    @Expose
    private int impressionSum;
    @SerializedName("date.day")
    @Expose
    private int dateDay;
    @SerializedName("ctr_percentage")
    @Expose
    private double ctrPercentage;
    @SerializedName("conversion_sum")
    @Expose
    private int conversionSum;
    @SerializedName("date.year")
    @Expose
    private int dateYear;
    @SerializedName("cost_avg")
    @Expose
    private double costAvg;

    /**
     *
     * @return
     * The clickSum
     */
    public int getClickSum() {
        return clickSum;
    }

    /**
     *
     * @param clickSum
     * The click_sum
     */
    public void setClickSum(int clickSum) {
        this.clickSum = clickSum;
    }

    /**
     *
     * @return
     * The dateMonth
     */
    public int getDateMonth() {
        return dateMonth;
    }

    /**
     *
     * @param dateMonth
     * The date.month
     */
    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    /**
     *
     * @return
     * The costSum
     */
    public double getCostSum() {
        return costSum;
    }

    /**
     *
     * @param costSum
     * The cost_sum
     */
    public void setCostSum(double costSum) {
        this.costSum = costSum;
    }

    /**
     *
     * @return
     * The impressionSum
     */
    public int getImpressionSum() {
        return impressionSum;
    }

    /**
     *
     * @param impressionSum
     * The impression_sum
     */
    public void setImpressionSum(int impressionSum) {
        this.impressionSum = impressionSum;
    }

    /**
     *
     * @return
     * The dateDay
     */
    public int getDateDay() {
        return dateDay;
    }

    /**
     *
     * @param dateDay
     * The date.day
     */
    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    /**
     *
     * @return
     * The ctrPercentage
     */
    public double getCtrPercentage() {
        return ctrPercentage;
    }

    /**
     *
     * @param ctrPercentage
     * The ctr_percentage
     */
    public void setCtrPercentage(double ctrPercentage) {
        this.ctrPercentage = ctrPercentage;
    }

    /**
     *
     * @return
     * The conversionSum
     */
    public int getConversionSum() {
        return conversionSum;
    }

    /**
     *
     * @param conversionSum
     * The conversion_sum
     */
    public void setConversionSum(int conversionSum) {
        this.conversionSum = conversionSum;
    }

    /**
     *
     * @return
     * The dateYear
     */
    public int getDateYear() {
        return dateYear;
    }

    /**
     *
     * @param dateYear
     * The date.year
     */
    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    /**
     *
     * @return
     * The costAvg
     */
    public double getCostAvg() {
        return costAvg;
    }

    /**
     *
     * @param costAvg
     * The cost_avg
     */
    public void setCostAvg(double costAvg) {
        this.costAvg = costAvg;
    }

}
