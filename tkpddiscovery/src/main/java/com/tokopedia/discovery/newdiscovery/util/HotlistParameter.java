package com.tokopedia.discovery.newdiscovery.util;

/**
 * Created by hangnadi on 10/7/17.
 */

public class HotlistParameter extends SearchParameter {

    public static final String SOURCE_HOTLIST = "hotlist";
    
    private String hotlistAlias;
    private int row;

    public String getHotlistAlias() {
        return hotlistAlias;
    }

    public void setHotlistAlias(String hotlistAlias) {
        this.hotlistAlias = hotlistAlias;
    }

}
