package com.tokopedia.seller.shop.open.constant;

/**
 * Created by Hendry on 3/22/2017.
 */

public final class ShopOpenNetworkConstant {
    public static final String BASE_PATH_APPS = "/v1/web-service/apps/";
    public static final String PATH_DOMAIN_CHECK = BASE_PATH_APPS + "domain_check";
    public static final String PATH_RESERVE_DOMAIN = BASE_PATH_APPS + "reserve_domain";
    public static final String PATH_IS_RESERVE_DOMAIN = BASE_PATH_APPS + "is_reserve_domain";
    public static final String PATH_RESERVE_SHOP_DESC_INFO = BASE_PATH_APPS + "add_shop_description";
    public static final String PATH_CREATE_SHOP = BASE_PATH_APPS + "create_shop";

    public static final String PARAM_SHOP_DOMAIN = "shop_domain";
    public static final String PARAM_SHOP_NAME = "shop_name";
}
