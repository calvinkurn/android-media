package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.NOT_USED;
import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.WITHOUT_CATALOG;
import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.WITH_CATALOG;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

@StringDef({WITH_CATALOG, WITHOUT_CATALOG, NOT_USED})
public @interface CatalogProductOption {
    String WITH_CATALOG = "1";
    String WITHOUT_CATALOG = "2";
    String NOT_USED = "-1";
}
