package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.topads;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 3/27/17.
 * Copied to feed by milhamj 1/18/17.
 */

public class Badge {

    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE_URL = "image_url";

    private String title;
    private String imageUrl;

    public Badge(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Badge(JSONObject object) throws JSONException {
        setTitle(object.getString(KEY_TITLE));
        setImageUrl(object.getString(KEY_IMAGE_URL));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
