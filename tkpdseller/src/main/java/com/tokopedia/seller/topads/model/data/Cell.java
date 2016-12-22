package com.tokopedia.seller.topads.model.data;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cell {

    @SerializedName("click_sum")
    @Expose
    private Integer clickSum;
    @SerializedName("date.month")
    @Expose
    private Integer dateMonth;
    @SerializedName("cost_sum")
    @Expose
    private Double costSum;
    @SerializedName("impression_sum")
    @Expose
    private Integer impressionSum;
    @SerializedName("date.day")
    @Expose
    private Integer dateDay;
    @SerializedName("ctr_percentage")
    @Expose
    private Double ctrPercentage;
    @SerializedName("conversion_sum")
    @Expose
    private Integer conversionSum;
    @SerializedName("date.year")
    @Expose
    private Integer dateYear;
    @SerializedName("cost_avg")
    @Expose
    private Double costAvg;

    /**
     *
     * @return
     * The clickSum
     */
    public Integer getClickSum() {
        return clickSum;
    }

    /**
     *
     * @param clickSum
     * The click_sum
     */
    public void setClickSum(Integer clickSum) {
        this.clickSum = clickSum;
    }

    /**
     *
     * @return
     * The dateMonth
     */
    public Integer getDateMonth() {
        return dateMonth;
    }

    /**
     *
     * @param dateMonth
     * The date.month
     */
    public void setDateMonth(Integer dateMonth) {
        this.dateMonth = dateMonth;
    }

    /**
     *
     * @return
     * The costSum
     */
    public Double getCostSum() {
        return costSum;
    }

    /**
     *
     * @param costSum
     * The cost_sum
     */
    public void setCostSum(Double costSum) {
        this.costSum = costSum;
    }

    /**
     *
     * @return
     * The impressionSum
     */
    public Integer getImpressionSum() {
        return impressionSum;
    }

    /**
     *
     * @param impressionSum
     * The impression_sum
     */
    public void setImpressionSum(Integer impressionSum) {
        this.impressionSum = impressionSum;
    }

    /**
     *
     * @return
     * The dateDay
     */
    public Integer getDateDay() {
        return dateDay;
    }

    /**
     *
     * @param dateDay
     * The date.day
     */
    public void setDateDay(Integer dateDay) {
        this.dateDay = dateDay;
    }

    /**
     *
     * @return
     * The ctrPercentage
     */
    public Double getCtrPercentage() {
        return ctrPercentage;
    }

    /**
     *
     * @param ctrPercentage
     * The ctr_percentage
     */
    public void setCtrPercentage(Double ctrPercentage) {
        this.ctrPercentage = ctrPercentage;
    }

    /**
     *
     * @return
     * The conversionSum
     */
    public Integer getConversionSum() {
        return conversionSum;
    }

    /**
     *
     * @param conversionSum
     * The conversion_sum
     */
    public void setConversionSum(Integer conversionSum) {
        this.conversionSum = conversionSum;
    }

    /**
     *
     * @return
     * The dateYear
     */
    public Integer getDateYear() {
        return dateYear;
    }

    /**
     *
     * @param dateYear
     * The date.year
     */
    public void setDateYear(Integer dateYear) {
        this.dateYear = dateYear;
    }

    /**
     *
     * @return
     * The costAvg
     */
    public Double getCostAvg() {
        return costAvg;
    }

    /**
     *
     * @param costAvg
     * The cost_avg
     */
    public void setCostAvg(Double costAvg) {
        this.costAvg = costAvg;
    }

}
