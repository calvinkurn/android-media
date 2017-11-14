
package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deadline {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("color")
    @Expose
    private String color;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
