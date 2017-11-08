
package com.tokopedia.events.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class HomeEntity {

    @SerializedName("layout")
    @Expose
    private JSONObject layout;

    public JSONObject getLayout() {
        return layout;
    }

    public void setLayout(JSONObject layout) {
        this.layout = layout;
    }

}
