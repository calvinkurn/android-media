package com.tokopedia.topads.dashboard.constant;

/**
 * Created by Nathaniel on 11/15/2016.
 */

public class TopAdsNetworkConstant {

    public static final String PATH_DASHBOARD_STATISTIC = "/v1.1/dashboard/statistics";
    public static final String PATH_DASHBOARD_DEPOSIT = "/v1.1/dashboard/deposit";
    public static final String PATH_DASHBOARD_TOTAL_AD = "/v1.1/dashboard/total_ad";
    public static final String PATH_SEARCH_PRODUCT = "/v1.1/dashboard/search_products";
    public static final String PATH_SEARCH_GROUP = "/v1.1/dashboard/search_groups";

    public static final String PATH_DASHBOARD_PRODUCT = "v1.1/dashboard/group_products";
    public static final String PATH_DASHBOARD_GROUP = "v1.1/dashboard/groups";
    public static final String PATH_DASHBOARD_SHOP = "/v1.1/dashboard/shop";
    public static final String PATH_DASHBOARD_CREDIT = "/v1/tkpd_products";

    public static final String PATH_BULK_ACTION_PRODUCT_AD = "/v2.1/promo/bulk";
    public static final String PATH_BULK_ACTION_GROUP_AD = "/v2.1/promo/group/bulk";
    public static final String PATH_DETAIL_PRODUCT_AD = "/v2.1/promo";

    public static final String PATH_CREATE_GROUP_AD = "v2.1/promo/group";

    public static final String GET_DASHBOARD_KEYWORD = "/v1.1/dashboard/keywords";
    public static final String ADD_KEYOWRD = "/v2.1/promo/keyword";
    public static final String PATH_EDIT_KEYWORD_DETAIL = "/v2.1/promo/keyword";
    public static final String PATH_BULK_KEYWORD_DETAIL = PATH_EDIT_KEYWORD_DETAIL + "/bulk";

    public static final String GET_SUGGESTION = "/v1/suggest";

    public static final String PARAM_AD = "ad";
    public static final String PARAM_SHOP_ID = "shop_id";
    public static final String PARAM_GROUP_ID = "group_id";
    public static final String PARAM_AD_ID = "ad_id";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_START_DATE = "start_date";
    public static final String PARAM_END_DATE = "end_date";
    public static final String PARAM_KEYWORD = "keyword";
    public static final String PARAM_ETALASE = "etalase";
    public static final String PARAM_SORT_BY = "sort_by";
    public static final String PARAM_START = "start";
    public static final String PARAM_ROWS = "rows";
    public static final String PARAM_STATUS = "status";
    public static final String PARAM_GROUP = "group";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_IS_PROMOTED = "is_promoted";
    public static final String PARAM_KEYWORD_AD_ID = "keyword_id";
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_ITEM_ID = "item_id";
    public static final String PARAM_SUGGESTION = "suggestion";

    public static final String VALUE_SOURCE_ANDROID = "android";
    public static final String VALUE_TOGGLE_ON = "on";
    public static final String VALUE_TOGGLE_OFF = "off";

    public static final String ACTION_BULK_DELETE_AD = "delete";
    public static final String ACTION_BULK_OFF_AD = "toggle_off";
    public static final String ACTION_BULK_ON_AD = "toggle_on";
    public static final String ACTION_BULK_MOVE_AD = "move_group";

    public static final String SOURCE_DASHBOARD_USER_MAIN = "dashboard_user_main";

    public static final int TYPE_PRODUCT_STAT = 1;
    public static final int TYPE_PRODUCT_SHOP = 2;

    public static final String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";
    public static final String SUGGESTION_TYPE_DEPARTMENT_ID = "dep_bid";
    public static final String SUGGESTION_TYPE_GROUP_ID = "group_bid";
    public static final String SUGGESTION_DATA_TYPE_DETAIL = "detail";
    public static final String SUGGESTION_DATA_TYPE_SUMMARY = "summary";
    public static final String SOURCE_GROUP_AD_LIST = "top_ads_group_ad_list";
    public static final String SOURCE_NEW_COST_GROUP = "top_ads_new_cost_new_group";
    public static final String SOURCE_NEW_COST_WITHOUT_GROUP = "top_ads_new_cost_without_group";
    public static final String SHOP_ID = "shop_id";
}