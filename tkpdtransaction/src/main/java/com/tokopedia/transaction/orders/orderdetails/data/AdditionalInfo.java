package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baghira on 11/05/18.
 */

public class AdditionalInfo {
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
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("urlDetail")
    @Expose
    private String urlDetail;
    @SerializedName("urlText")
    @Expose
    private String urlText;

    public AdditionalInfo(String label, String value, String textColor, String backgroundColor, String imageUrl, String notes, String urlDetail, String urlText) {
        this.label = label;
        this.value = value;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
        this.notes = notes;
        this.urlDetail = urlDetail;
        this.urlText = urlText;
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

    public String getNotes() {
        return notes;
    }

    public String getUrlDetail() {
        return urlDetail;
    }

    public String getUrlText() {
        return urlText;
    }

    @Override
    public String toString() {
        return "AdditionalInfo{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", textColor='" + textColor + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", notes='" + notes + '\'' +
                ", urlDetail='" + urlDetail + '\'' +
                ", urlText='" + urlText + '\'' +
                '}';
    }
}