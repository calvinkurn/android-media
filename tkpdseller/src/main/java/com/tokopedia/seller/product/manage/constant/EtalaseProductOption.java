package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.NOT_USED;
import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.WITHOUT_CATALOG;
import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.WITH_CATALOG;
import static com.tokopedia.seller.product.manage.constant.EtalaseProductOption.ALL_SHOWCASE;
import static com.tokopedia.seller.product.manage.constant.EtalaseProductOption.NOT_AVAILABLE;
import static com.tokopedia.seller.product.manage.constant.EtalaseProductOption.PREORDER;
import static com.tokopedia.seller.product.manage.constant.EtalaseProductOption.PRODUCT_SOLD;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

@StringDef({NOT_AVAILABLE, PREORDER, ALL_SHOWCASE, PRODUCT_SOLD})
public @interface EtalaseProductOption {
    String NOT_AVAILABLE = "warehouse";
    String PREORDER = "preorder";
    String ALL_SHOWCASE = "etalase";
    String PRODUCT_SOLD = "sold";
}
