package com.tokopedia.topads.sdk.base;

/**
 * @author by errysuprayogi on 5/15/17.
 */

public enum Endpoint {
    RANDOM("0"), PRODUCT("1"), SHOP("2"), CPM("3");

    private String description;

    private Endpoint(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
