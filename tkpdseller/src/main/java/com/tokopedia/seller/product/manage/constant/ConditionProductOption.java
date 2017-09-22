package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.seller.product.manage.constant.ConditionProductOption.NEW;
import static com.tokopedia.seller.product.manage.constant.ConditionProductOption.NOT_USED;
import static com.tokopedia.seller.product.manage.constant.ConditionProductOption.USED;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

@StringDef({NEW, USED, NOT_USED})
public @interface ConditionProductOption {
    String NEW = "1";
    String USED = "2";
    String NOT_USED = "-1";
}
