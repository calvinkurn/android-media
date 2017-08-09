
package com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValueIndicator {

    @SerializedName("indicator_type")
    @Expose
    private String indicatorType;
    @SerializedName("indicator_desc")
    @Expose
    private String indicatorDesc;
    @SerializedName("indicator_scoring")
    @Expose
    private List<IndicatorScoring> indicatorScoring = null;

    public String getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(String indicatorType) {
        this.indicatorType = indicatorType;
    }

    public String getIndicatorDesc() {
        return indicatorDesc;
    }

    public void setIndicatorDesc(String indicatorDesc) {
        this.indicatorDesc = indicatorDesc;
    }

    public List<IndicatorScoring> getIndicatorScoring() {
        return indicatorScoring;
    }

    public void setIndicatorScoring(List<IndicatorScoring> indicatorScoring) {
        this.indicatorScoring = indicatorScoring;
    }

}
