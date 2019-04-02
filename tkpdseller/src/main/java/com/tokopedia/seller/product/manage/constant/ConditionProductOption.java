package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.product.manage.constant.ConditionProductOption.NEW;
import static com.tokopedia.seller.product.manage.constant.ConditionProductOption.ALL_CONDITION;
import static com.tokopedia.seller.product.manage.constant.ConditionProductOption.USED;

/**
 * Created by zulfikarrahman on 9/22/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({NEW, USED, ALL_CONDITION})
public @interface ConditionProductOption {
    String NEW = "1";
    String USED = "2";
    String ALL_CONDITION = "-1";
}
