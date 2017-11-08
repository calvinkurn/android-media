package com.tokopedia.events.domain.model;

import org.json.JSONObject;

/**
 * Created by ashwanityagi on 06/11/17.
 */

public class Home {

    private JSONObject layout;

    public JSONObject getLayout() {
        return layout;
    }

    public void setLayout(JSONObject layout) {
        this.layout = layout;
    }
}
