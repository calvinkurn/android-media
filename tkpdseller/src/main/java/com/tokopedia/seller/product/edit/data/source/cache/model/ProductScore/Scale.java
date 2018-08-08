
package com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Scale {

    @SerializedName("min")
    @Expose
    private int min;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("text")
    @Expose
    private String text;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
