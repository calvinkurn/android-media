package com.tokopedia.ride.common.ride.data.entity;

/**
 * Created by vishal.gupta on 4/3/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FareBreakdownEntity {

    @SerializedName("low_amount")
    @Expose
    private Double lowAmount;
    @SerializedName("high_amount")
    @Expose
    private Double highAmount;
    @SerializedName("display_amount")
    @Expose
    private String displayAmount;
    @SerializedName("display_name")
    @Expose
    private String displayName;

    public Double getLowAmount() {
        return lowAmount;
    }

    public void setLowAmount(Double lowAmount) {
        this.lowAmount = lowAmount;
    }

    public Double getHighAmount() {
        return highAmount;
    }

    public void setHighAmount(Double highAmount) {
        this.highAmount = highAmount;
    }

    public String getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(String displayAmount) {
        this.displayAmount = displayAmount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}