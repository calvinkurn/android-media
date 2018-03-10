
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoQuality {

    @SerializedName("average")
    @Expose
    private String average;
    @SerializedName("count_total")
    @Expose
    private String countTotal;
    @SerializedName("five_star_rank")
    @Expose
    private String fiveStarRank;
    @SerializedName("four_star_rank")
    @Expose
    private String fourStarRank;
    @SerializedName("one_star_rank")
    @Expose
    private String oneStarRank;
    @SerializedName("rating_star")
    @Expose
    private float ratingStar;
    @SerializedName("three_star_rank")
    @Expose
    private String threeStarRank;
    @SerializedName("two_star_rank")
    @Expose
    private String twoStarRank;

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(String countTotal) {
        this.countTotal = countTotal;
    }

    public String getFiveStarRank() {
        return fiveStarRank;
    }

    public void setFiveStarRank(String fiveStarRank) {
        this.fiveStarRank = fiveStarRank;
    }

    public String getFourStarRank() {
        return fourStarRank;
    }

    public void setFourStarRank(String fourStarRank) {
        this.fourStarRank = fourStarRank;
    }

    public String getOneStarRank() {
        return oneStarRank;
    }

    public void setOneStarRank(String oneStarRank) {
        this.oneStarRank = oneStarRank;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getThreeStarRank() {
        return threeStarRank;
    }

    public void setThreeStarRank(String threeStarRank) {
        this.threeStarRank = threeStarRank;
    }

    public String getTwoStarRank() {
        return twoStarRank;
    }

    public void setTwoStarRank(String twoStarRank) {
        this.twoStarRank = twoStarRank;
    }

}
