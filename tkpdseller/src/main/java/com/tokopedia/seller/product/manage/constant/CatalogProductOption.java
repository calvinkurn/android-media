package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.WITH_AND_WITHOUT;
import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.WITHOUT_CATALOG;
import static com.tokopedia.seller.product.manage.constant.CatalogProductOption.WITH_CATALOG;

/**
 * Created by zulfikarrahman on 9/22/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({WITH_CATALOG, WITHOUT_CATALOG, WITH_AND_WITHOUT})
public @interface CatalogProductOption {
    String WITH_CATALOG = "1";
    String WITHOUT_CATALOG = "2";
    String WITH_AND_WITHOUT = "-1";
}
