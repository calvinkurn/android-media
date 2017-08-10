
package com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IndicatorScore {

    @SerializedName("indicator_id")
    @Expose
    private int indicatorId;
    @SerializedName("name_indicator")
    @Expose
    private String nameIndicator;
    @SerializedName("value_indicator")
    @Expose
    private List<ValueIndicator> valueIndicator = null;
    @SerializedName("color_indicator")
    @Expose
    private List<ColorIndicator> colorIndicator = null;

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public String getNameIndicator() {
        return nameIndicator;
    }

    public void setNameIndicator(String nameIndicator) {
        this.nameIndicator = nameIndicator;
    }

    public List<ValueIndicator> getValueIndicator() {
        return valueIndicator;
    }

    public void setValueIndicator(List<ValueIndicator> valueIndicator) {
        this.valueIndicator = valueIndicator;
    }

    public List<ColorIndicator> getColorIndicator() {
        return colorIndicator;
    }

    public void setColorIndicator(List<ColorIndicator> colorIndicator) {
        this.colorIndicator = colorIndicator;
    }

}
