package com.tokopedia.seller.shop.setting.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.shop.setting.constant.ShopCloseAction.ABORT_CLOSE_SCHEDULE;
import static com.tokopedia.seller.shop.setting.constant.ShopCloseAction.CLOSE_SHOP;
import static com.tokopedia.seller.shop.setting.constant.ShopCloseAction.EXTEND_CLOSE_SCHEDULE;
import static com.tokopedia.seller.shop.setting.constant.ShopCloseAction.OPEN_SHOP;
import static com.tokopedia.seller.shop.setting.constant.ShopCloseAction.SET_CLOSE_SCHEDULE;


/**
 * Created by ZulfikarRahman on 9/15/17.
 */


@IntDef({OPEN_SHOP, CLOSE_SHOP, SET_CLOSE_SCHEDULE, ABORT_CLOSE_SCHEDULE, EXTEND_CLOSE_SCHEDULE})
@Retention(RetentionPolicy.SOURCE)
public @interface ShopCloseAction {
    int OPEN_SHOP = 1;
    int CLOSE_SHOP = 2;
    int SET_CLOSE_SCHEDULE = 3;
    int ABORT_CLOSE_SCHEDULE = 4;
    int EXTEND_CLOSE_SCHEDULE = 5;
}
