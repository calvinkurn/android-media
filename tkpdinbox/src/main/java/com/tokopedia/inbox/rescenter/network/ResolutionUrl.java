package com.tokopedia.inbox.rescenter.network;

/**
 * @author by yfsx on 26/07/18.
 */
public class ResolutionUrl {

    public static String BASE_URL = "https://api.tokopedia.com/";

    public static final String BASE_RESOLUTION = BASE_URL + "resolution/";
    public static final String BASE_RESOLUTION_VERSION_1 = "resolution/v1/";
    public static final String BASE_RESOLUTION_VERSION_2 = "resolution/v2/";
    public static final String BASE_INBOX_RESOLUTION = BASE_RESOLUTION_VERSION_1 + "inbox";
    public static final String BASE_INBOX_RESOLUTION_V2 = BASE_RESOLUTION_VERSION_2 + "inbox";
    public static final String BASE_DETAIL_RESOLUTION = BASE_RESOLUTION_VERSION_1 + "detail/{resolution_id}";
    public static final String BASE_DETAIL_RESOLUTION_V2 = BASE_RESOLUTION_VERSION_2 + "detail/{resolution_id}";

    public static final String BASE_RESOLUTION_CREATE = BASE_RESOLUTION_VERSION_2 + "create/{order_id}";
    public static final String BASE_RESOLUTION_VALIDATE = BASE_RESOLUTION_VERSION_2 + "create/{order_id}";
    public static final String BASE_RESOLUTION_SUBMIT = BASE_RESOLUTION_VERSION_2 + "create/{order_id}";
    public static final String GET_RESOLUTION_STEP_1 = BASE_RESOLUTION_CREATE + "/step1";
    public static final String POST_RESOLUTION_STEP_2_3 = BASE_RESOLUTION_CREATE + "/step2_3";
}
