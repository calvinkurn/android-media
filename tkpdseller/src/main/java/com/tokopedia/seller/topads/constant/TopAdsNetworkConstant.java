package com.tokopedia.seller.topads.constant;

/**
 * Created by Nathaniel on 11/15/2016.
 */

public class TopAdsNetworkConstant {

    public static final String PATH_DASHBOARD_STATISTIC = "/v1.1/dashboard/statistics";
    public static final String PATH_DASHBOARD_DEPOSIT = "/v1.1/dashboard/deposit";
    public static final String PATH_DASHBOARD_TOTAL_AD = "/v1.1/dashboard/total_ad";

    public static final String PATH_DASHBOARD_PRODUCT = "v1.1/dashboard/products";
    public static final String PATH_DASHBOARD_GROUP = "v1.1/dashboard/groups";
    public static final String PATH_DASHBOARD_SHOP = "/v1/dashboard/shop";
    public static final String PATH_DASHBOARD_CREDIT = "/v1/tkpd_products";

    public static final String PATH_ACTION_BULK_AD = "/v2/promo/bulk";

    public static final String PARAM_SHOP_ID = "shop_id";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_START_DATE = "start_date";
    public static final String PARAM_END_DATE = "end_date";

    public static final String ACTION_BULK_DELETE_AD = "delete";
    public static final String ACTION_BULK_OFF_AD = "toogle_off";
    public static final String ACTION_BULK_ON_AD = "toogle_on";
    public static final String ACTION_BULK_MOVE_AD = "move_group";
}
