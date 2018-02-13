package com.tokopedia.inbox.rescenter.inboxv2.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yfsx on 24/01/18.
 */
public class StatusResponse {

    @SerializedName("int")
    private int intX;
    @SerializedName("string")
    private String string;
    @SerializedName("fontColor")
    private String fontColor;
    @SerializedName("bgColor")
    private String bgColor;

    public int getIntX() {
        return intX;
    }

    public void setIntX(int intX) {
        this.intX = intX;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
}
