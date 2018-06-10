package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class CpmImage {

    private static final String KEY_FULL_URL = "full_url";
    private static final String KEY_FULL_ECS = "full_ecs";
    private static final String KEY_ILUSTRATION_URL = "illustration_url";

    private String fullUrl;
    private String fullEcs;
    private String ilustrationUrl;

    public CpmImage(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_FULL_URL)){
            setFullUrl(object.getString(KEY_FULL_URL));
        }
        if(!object.isNull(KEY_FULL_ECS)){
            setFullEcs(object.getString(KEY_FULL_ECS));
        }
        if(!object.isNull(KEY_ILUSTRATION_URL)){
            setIlustrationUrl(object.getString(KEY_ILUSTRATION_URL));
        }
    }

    public void setIlustrationUrl(String ilustrationUrl) {
        this.ilustrationUrl = ilustrationUrl;
    }

    public String getIlustrationUrl() {
        return ilustrationUrl;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getFullEcs() {
        return fullEcs;
    }

    public void setFullEcs(String fullEcs) {
        this.fullEcs = fullEcs;
    }
}

