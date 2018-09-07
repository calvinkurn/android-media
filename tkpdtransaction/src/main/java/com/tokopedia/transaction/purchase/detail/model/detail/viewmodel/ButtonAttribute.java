package com.tokopedia.transaction.purchase.detail.model.detail.viewmodel;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class ButtonAttribute {

    public static final int GREEN_COLOR_MODE = 0;
    public static final int WHITE_COLOR_MODE = 1;

    public static final int FINISH_ID = 50;
    public static final int VIEW_COMPLAINT_ID = 70;
    public static final int RECEIVED_PACKAGE_ID = 80;
    public static final int TRACK_ID = 100;
    public static final int ASK_SELLER_ID = 200;
    public static final int ASK_BUYER_ID = 300;
    public static final int REQUEST_CANCEL_ID = 3000;
    public static final int COMPLAINT_ID = 4000;
    public static final int CANCEL_SEARCH_ID = 5000;

    private String buttonText;

    private int buttonColorMode;

    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
