
package com.tokopedia.seller.product.edit.view.model.scoringproduct;

import java.util.List;

public class DataScoringProductView {
    private TotalScoringProductView totalScoringProductView;
    private List<IndicatorScoreView> indicatorScoreView = null;

    public TotalScoringProductView getTotalScoringProductView() {
        return totalScoringProductView;
    }

    public void setTotalScoringProductView(TotalScoringProductView totalScoringProductView) {
        this.totalScoringProductView = totalScoringProductView;
    }

    public List<IndicatorScoreView> getIndicatorScoreView() {
        return indicatorScoreView;
    }

    public void setIndicatorScoreView(List<IndicatorScoreView> indicatorScoreView) {
        this.indicatorScoreView = indicatorScoreView;
    }

}
