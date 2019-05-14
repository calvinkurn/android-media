package com.tokopedia.inbox.rescenter.network;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * @author by yfsx on 26/07/18.
 */
public class ResolutionUrl {

    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getAPI();
    public static String BASE_URL_IMAGE_SERVICE = TokopediaUrl.Companion.getInstance().getWS();


    public static final String BASE_RESOLUTION = BASE_URL + "resolution/";
    public static final String BASE_RESOLUTION_VERSION_1 = "resolution/v1/";
    public static final String BASE_RESOLUTION_VERSION_2 = "resolution/v2/";
    public static final String BASE_RESOLUTION_VERSION_3 = "resolution/v3/";
    public static final String BASE_RESOLUTION_VERSION = BASE_RESOLUTION_VERSION_3;

    public static final String BASE_RESOLUTION_CREATE = BASE_RESOLUTION_VERSION + "create/{order_id}";
    public static final String BASE_RESOLUTION_VALIDATE = BASE_RESOLUTION_VERSION + "create/{order_id}";
    public static final String BASE_RESOLUTION_SUBMIT = BASE_RESOLUTION_VERSION + "create/{order_id}";
    public static final String GET_RESOLUTION_STEP_1 = BASE_RESOLUTION_CREATE + "/step/1";
    public static final String POST_RESOLUTION_STEP_2_3 = BASE_RESOLUTION_CREATE + "/step/2";
    public static final String BASE_RESOLUTION_DETAIL = BASE_RESOLUTION_VERSION + "detail/{resolution_id}";
    public static final String GET_RESOLUTION_EDIT = BASE_RESOLUTION_DETAIL + "/edit";
    public static final String POST_RESOLUTION_EDIT = BASE_RESOLUTION_DETAIL + "/edit";
    public static final String GET_RESOLUTION_APPEAL = BASE_RESOLUTION_DETAIL + "/appeal";
    public static final String POST_RESOLUTION_APPEAL = BASE_RESOLUTION_DETAIL + "/appeal";


    public static final String BASE_RESOLUTION_RECOMPLAINT = BASE_RESOLUTION_DETAIL + "/recomplain";
    public static final String GET_RESOLUTION_RECOMPLAINT_STEP_1 = BASE_RESOLUTION_RECOMPLAINT + "/step/1";
    public static final String POST_RESOLUTION_RECOMPLAINT_STEP_2_3 = BASE_RESOLUTION_RECOMPLAINT + "/step/2";


    public static final String PATH_GENERATE_TOKEN_HOST_WITHOUT_HEADER = "generate_token_host.pl";
    public static final String PATH_GENERATE_TOKEN_HOST = BASE_RESOLUTION_VERSION_1 + "generate_host";
}

