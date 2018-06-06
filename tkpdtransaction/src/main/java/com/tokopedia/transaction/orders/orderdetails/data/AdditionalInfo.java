package com.tokopedia.transaction.orders.orderdetails.data;

/**
 * Created by baghira on 11/05/18.
 */

public class AdditionalInfo {
    private String label;
    private String value;
    private String textColor;
    private String backgroundColor;
    private String imageUrl;

    public AdditionalInfo(String label, String value, String textColor, String backgroundColor, String imageUrl) {
        this.label = label;
        this.value = value;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.imageUrl = imageUrl;
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
        return "[AdditionalInfo:{"
                + "label="+label +" "
                + "value="+value +" "
                + "textColor="+textColor +" "
                + "backgroundColor="+backgroundColor +" "
                + "imageUrl="+imageUrl
                + "}]";
    }
}
