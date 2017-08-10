package com.tokopedia.topads.sdk.view;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public enum DisplayMode {

    GRID(0),
    LIST(1),
    FEED(3), //Condition if TopAds shop will display 1 item only
    FEED_EMPTY(4);

    private int value;

    DisplayMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
