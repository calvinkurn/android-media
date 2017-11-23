
package com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataScoringProduct {

    @SerializedName("total_scoring_product")
    @Expose
    private TotalScoringProduct totalScoringProduct;
    @SerializedName("indicator_score")
    @Expose
    private List<IndicatorScore> indicatorScore = null;

    public TotalScoringProduct getTotalScoringProduct() {
        return totalScoringProduct;
    }

    public void setTotalScoringProduct(TotalScoringProduct totalScoringProduct) {
        this.totalScoringProduct = totalScoringProduct;
    }

    public List<IndicatorScore> getIndicatorScore() {
        return indicatorScore;
    }

    public void setIndicatorScore(List<IndicatorScore> indicatorScore) {
        this.indicatorScore = indicatorScore;
    }

}
