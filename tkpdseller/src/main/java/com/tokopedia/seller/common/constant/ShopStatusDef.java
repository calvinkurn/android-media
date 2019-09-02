package com.tokopedia.seller.common.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.common.constant.ShopStatusDef.CLOSED;
import static com.tokopedia.seller.common.constant.ShopStatusDef.DELETED;
import static com.tokopedia.seller.common.constant.ShopStatusDef.MODERATED;
import static com.tokopedia.seller.common.constant.ShopStatusDef.MODERATED_PERMANENTLY;
import static com.tokopedia.seller.common.constant.ShopStatusDef.NOT_ACTIVE;
import static com.tokopedia.seller.common.constant.ShopStatusDef.OPEN;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({OPEN, NOT_ACTIVE, CLOSED, DELETED, MODERATED, MODERATED_PERMANENTLY})
public @interface ShopStatusDef {
    int OPEN = 1;
    int NOT_ACTIVE = 4;
    int CLOSED = 2;
    int DELETED = 0;
    int MODERATED = 3;
    int MODERATED_PERMANENTLY = 5;
}
