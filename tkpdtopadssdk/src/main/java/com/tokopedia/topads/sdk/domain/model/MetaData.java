package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 */
public class MetaData {

    private String display;

    private static final String KEY_DISPLAY = "display";

    public MetaData(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_DISPLAY)) {
            setDisplay(object.getString(KEY_DISPLAY));
        }
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
