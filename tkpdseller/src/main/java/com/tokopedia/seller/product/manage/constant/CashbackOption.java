package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.product.manage.constant.CashbackOption.CASHBACK_OPTION_3;
import static com.tokopedia.seller.product.manage.constant.CashbackOption.CASHBACK_OPTION_4;
import static com.tokopedia.seller.product.manage.constant.CashbackOption.CASHBACK_OPTION_5;
import static com.tokopedia.seller.product.manage.constant.CashbackOption.CASHBACK_OPTION_NONE;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

@IntDef({CASHBACK_OPTION_3, CASHBACK_OPTION_4, CASHBACK_OPTION_5, CASHBACK_OPTION_NONE})
public @interface CashbackOption {
    int CASHBACK_OPTION_3 = 3;
    int CASHBACK_OPTION_4 = 4;
    int CASHBACK_OPTION_5 = 5;
    int CASHBACK_OPTION_NONE = 0;
}
