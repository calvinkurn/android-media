package com.tokopedia.tkpdstream.common.analytics;

/**
 * @author by nisie on 3/29/18.
 */

public class EEPromotion {

    private static final String NAME_PROFILE_PAGE = "/profile page";
    private static final String KOL_POST = "kolpost";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CREATIVE = "creative";
    public static final String KEY_POSITION = "position";
    public static final String KEY_PROMO_ID = "promo_id";
    public static final String KEY_PROMO_CODE = "promo_code";
    public static final String KEY_CREATIVE_URL = "creative_url";
    public static final String ATTRIBUTION = "attribution";
    public static final String NAME_GROUPCHAT = "/groupchat";

    String id;
    String name;
    int position;
    String creative;
    String creativeUrl;
    String attribution;

    public EEPromotion(String id, String name, int position, String creative, String creativeUrl,
                       String attribution) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.creative = creative;
        this.creativeUrl = creativeUrl;
        this.attribution = attribution;
    }

    /**
     * banner_id
     */
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreative() {
        return creative;
    }

    public int getPosition() {
        return position;
    }

    public String getCreativeUrl() {
        return creativeUrl;
    }

    public String getAttribution() {
        return attribution;
    }
}
