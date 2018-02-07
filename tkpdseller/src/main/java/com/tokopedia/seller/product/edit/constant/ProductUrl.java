package com.tokopedia.seller.product.edit.constant;

/**
 * Created by zulfikarrahman on 2/5/18.
 */

public class ProductUrl {
    public static final String URL_ADD_PRODUCT = "/v2.1/product";
    public static final String PRODUCT_ID = "product_id";
    public static final String URL_EDIT_PRODUCT = URL_ADD_PRODUCT + "/{" + PRODUCT_ID + "}";
}
