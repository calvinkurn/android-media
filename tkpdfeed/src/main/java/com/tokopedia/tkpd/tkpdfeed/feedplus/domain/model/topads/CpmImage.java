package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 12/28/17.
 * Copied to feed by milhamj 1/18/17.
 */

public class CpmImage {

    private static final String KEY_FULL_URL = "full_url";
    private static final String KEY_FULL_ECS = "full_ecs";

    private String fullUrl;
    private String fullEcs;

    public CpmImage(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_FULL_URL)){
            setFullUrl(object.getString(KEY_FULL_URL));
        }
        if(!object.isNull(KEY_FULL_ECS)){
            setFullEcs(object.getString(KEY_FULL_ECS));
        }
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

