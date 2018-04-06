package com.tokopedia.shop.common.constant;

import com.tokopedia.abstraction.constant.TkpdAppLink;

/**
 * Created by zulfikarrahman on 2/28/18.
 */

public class ShopAppLink extends TkpdAppLink {

    public static final String SHOP = "tokopedia://shop/{shop_id}";
    public static final String SHOP_ETALASE = "tokopedia://shop/{shop_id}/etalase/{etalase_id}";
    public static final String SHOP_TALK = "tokopedia://shop/{shop_id}/talk";
    public static final String SHOP_REVIEW = "tokopedia://shop/{shop_id}/review";
    public static final String SHOP_NOTE = "tokopedia://shop/{shop_id}/note";
    public static final String SHOP_INFO = "tokopedia://shop/{shop_id}/info";
}
