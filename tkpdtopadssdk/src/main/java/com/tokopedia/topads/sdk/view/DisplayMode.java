package com.tokopedia.topads.sdk.view;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public enum DisplayMode {

    GRID(0), LIST(1), FEED(3);

    private int value;

    private DisplayMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
