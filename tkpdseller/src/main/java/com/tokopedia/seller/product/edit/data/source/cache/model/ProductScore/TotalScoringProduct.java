
package com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TotalScoringProduct {

    @SerializedName("max_score")
    @Expose
    private int maxScore;
    @SerializedName("scale")
    @Expose
    private List<Scale> scale = null;

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public List<Scale> getScale() {
        return scale;
    }

    public void setScale(List<Scale> scale) {
        this.scale = scale;
    }

}
