package com.tokopedia.seller.topads.data.model.data;

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
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tokopedia.seller.database.TkpdSellerDatabase;

@Table(database = TkpdSellerDatabase.class,
        uniqueColumnGroups = {@UniqueGroup(groupNumber = 1, uniqueConflict = ConflictAction.REPLACE)})
public class Summary extends BaseModel {

    @ContainerKey("id")
    @Column
    @Unique(unique = false)
    @PrimaryKey(autoincrement = true)
    public long Id;

    @SerializedName("click_sum")
    @Expose
    @Column
    public int clickSum;

    @SerializedName("click_sum_fmt")
    @Expose
    public String clickSumFmt;

    @SerializedName("cost_sum")
    @Expose
    @Column
    public double costSum;

    @SerializedName("cost_sum_fmt")
    @Expose
    public String costSumFmt;

    @SerializedName("impression_sum")
    @Expose
    @Column
    public int impressionSum;

    @SerializedName("impression_sum_fmt")
    @Expose
    public String impressionSumFmt;

    @SerializedName("ctr_percentage")
    @Expose
    @Column
    public double ctrPercentage;

    @SerializedName("ctr_percentage_fmt")
    @Expose
    public String ctrPercentageFmt;

    @SerializedName("conversion_sum")
    @Expose
    @Column
    public int conversionSum;

    @SerializedName("conversion_sum_fmt")
    @Expose
    public String conversionSumFmt;

    @SerializedName("cost_avg")
    @Expose
    @Column
    public double costAvg;

    @SerializedName("cost_avg_fmt")
    @Expose
    public String costAvgFmt;

    @Unique(unique = false, uniqueGroups = 1)
    @Column
    public String shopId;

    @Unique(unique = false, uniqueGroups = 1)
    @Column
    public int type;

    @Unique(unique = false, uniqueGroups = 1)
    @Column
    public String startDate;

    @Unique(unique = false, uniqueGroups = 1)
    @Column
    public String endDate;

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