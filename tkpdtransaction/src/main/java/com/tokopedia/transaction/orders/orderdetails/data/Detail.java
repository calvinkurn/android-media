package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baghira on 11/05/18.
 */

public class Detail {
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("textColor")
    @Expose
    private String textColor;
    @SerializedName("backgroundColor")
    @Expose
    private String backgroundColor;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public Detail(String label, String value, String textColor, String backgroundColor, String imageUrl) {
        this.label = label;
        this.value = value;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
    }

    public Detail(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String label() {
        return label;
    }

    public String value() {
        return value;
    }

    public String textColor() {
        return textColor;
    }

    public String backgroundColor() {
        return backgroundColor;
    }

    public String imageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "[Detail:{"
                + "label="+label +" "
                + "value="+value +" "
                + "textColor="+textColor +" "
                + "backgroundColor="+backgroundColor +" "
                + "imageUrl="+imageUrl
                + "}]";
    }
}
