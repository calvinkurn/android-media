
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoAccuracyWidth {

    @SerializedName("five_star_rank")
    @Expose
    private long fiveStarRank;
    @SerializedName("four_star_rank")
    @Expose
    private double fourStarRank;
    @SerializedName("one_star_rank")
    @Expose
    private double oneStarRank;
    @SerializedName("three_star_rank")
    @Expose
    private double threeStarRank;
    @SerializedName("two_star_rank")
    @Expose
    private double twoStarRank;

    public long getFiveStarRank() {
        return fiveStarRank;
    }

    public void setFiveStarRank(long fiveStarRank) {
        this.fiveStarRank = fiveStarRank;
    }

    public double getFourStarRank() {
        return fourStarRank;
    }

    public void setFourStarRank(double fourStarRank) {
        this.fourStarRank = fourStarRank;
    }

    public double getOneStarRank() {
        return oneStarRank;
    }

    public void setOneStarRank(double oneStarRank) {
        this.oneStarRank = oneStarRank;
    }

    public double getThreeStarRank() {
        return threeStarRank;
    }

    public void setThreeStarRank(double threeStarRank) {
        this.threeStarRank = threeStarRank;
    }

    public double getTwoStarRank() {
        return twoStarRank;
    }

    public void setTwoStarRank(double twoStarRank) {
        this.twoStarRank = twoStarRank;
    }

}
