package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.seller.product.manage.constant.SortProductOption.HIGHEST_PRICE;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.LAST_UPDATE;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.LOWEST_PRICE;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.MOST_BUY;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.MOST_REVIEW;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.MOST_TALK;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.MOST_VIEW;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.NEW_PRODUCT;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.POSITION;
import static com.tokopedia.seller.product.manage.constant.SortProductOption.PRODUCT_NAME;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

@StringDef({POSITION, NEW_PRODUCT, LAST_UPDATE, PRODUCT_NAME, MOST_VIEW, MOST_TALK, MOST_REVIEW, MOST_BUY, LOWEST_PRICE, HIGHEST_PRICE})
public @interface SortProductOption {
    String POSITION = "1";
    String NEW_PRODUCT = "2";
    String LAST_UPDATE = "3";
    String PRODUCT_NAME = "4";
    String MOST_VIEW = "5";
    String MOST_TALK = "6";
    String MOST_REVIEW = "7";
    String MOST_BUY = "8";
    String LOWEST_PRICE = "9";
    String HIGHEST_PRICE = "10";
}
