package com.tokopedia.seller.topads.model.data;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("click_sum")
    @Expose
    private Integer clickSum;
    @SerializedName("cost_sum")
    @Expose
    private Double costSum;
    @SerializedName("impression_sum")
    @Expose
    private Integer impressionSum;
    @SerializedName("ctr_percentage")
    @Expose
    private Double ctrPercentage;
    @SerializedName("conversion_sum")
    @Expose
    private Integer conversionSum;
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
