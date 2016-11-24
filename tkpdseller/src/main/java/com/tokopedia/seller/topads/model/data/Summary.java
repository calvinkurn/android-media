package com.tokopedia.seller.topads.model.data;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ContainerKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.seller.database.TkpdSellerDatabase;

@Table(database = TkpdSellerDatabase.class)
public class Summary extends BaseModel {

    @ContainerKey("id")
    @Column
    @PrimaryKey(autoincrement = true)
    public long Id;

    @SerializedName("click_sum")
    @Expose
    @Column
    public int clickSum;

    @SerializedName("cost_sum")
    @Expose
    @Column
    public double costSum;

    @SerializedName("impression_sum")
    @Expose
    @Column
    public int impressionSum;

    @SerializedName("ctr_percentage")
    @Expose
    @Column
    public double ctrPercentage;

    @SerializedName("conversion_sum")
    @Expose
    @Column
    public int conversionSum;

    @SerializedName("cost_avg")
    @Expose
    @Column
    public double costAvg;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String shopId;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public int type;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String startDate;

    @Unique(onUniqueConflict = ConflictAction.REPLACE)
    @Column
    public String endDate;
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
