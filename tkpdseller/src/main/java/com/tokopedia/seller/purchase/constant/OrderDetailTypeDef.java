package com.tokopedia.seller.purchase.constant;

import android.support.annotation.IntDef;

import static com.tokopedia.seller.purchase.constant.OrderDetailTypeDef.GREEN_BUTTON;
import static com.tokopedia.seller.purchase.constant.OrderDetailTypeDef.HIDE_BUTTON;
import static com.tokopedia.seller.purchase.constant.OrderDetailTypeDef.WHITE_BUTTON;


/**
 * Created by nabillasabbaha on 12/7/17.
 */

@IntDef({HIDE_BUTTON, WHITE_BUTTON, GREEN_BUTTON})
public @interface OrderDetailTypeDef {
    int HIDE_BUTTON = 0;
    int WHITE_BUTTON = 1;
    int GREEN_BUTTON = 2;
}
