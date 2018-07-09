package com.tokopedia.transaction.orders.orderdetails.data;

/**
 * Created by baghira on 10/05/18.
 */

public class Status {
    private String statusText;
    private String status;
    private String statusLabel;
    private String iconUrl;
    private String textColor;
    private String backgroundColor;
    private String fontSize;

    public Status(String statusText, String status, String statusLabel, String iconUrl, String textColor, String backgroundColor, String fontSize) {
        this.statusText = statusText;
        this.status = status;
        this.statusLabel = statusLabel;
        this.iconUrl = iconUrl;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.fontSize = fontSize;
    }

    public String statusText() {
        return statusText;
    }

    public String status() {
        return status;
    }

    public String statusLabel() {
        return statusLabel;
    }

    public String iconUrl() {
        return iconUrl;
    }

    public String textColor() {
        return textColor;
    }

    public String backgroundColor() {
        return backgroundColor;
    }

    public String fontSize() {
        return fontSize;
    }

    @Override
    public String toString() {
        return "[Status:{" +" "+
                "statusText=" + statusText +" "+
                "status=" + status + " "+
                "statusLabel=" + statusLabel + " "+
                "iconUrl=" + iconUrl + " "+
                "textColor=" + textColor + " "+
                "backgroundColor=" + backgroundColor + " "+
                "fontSize=" + fontSize
                + "}]";
    }
}
