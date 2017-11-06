package com.tokopedia.transaction.purchase.detail.model;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class ButtonAttribute {

    public static final int GREEN_COLOR_MODE = 0;
    public static final int WHITE_COLOR_MODE = 1;

    private String buttonText;

    private int buttonColorMode;

    private int visibility;

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getButtonColorMode() {
        return buttonColorMode;
    }

    public void setButtonColorMode(int buttonColorMode) {
        this.buttonColorMode = buttonColorMode;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
