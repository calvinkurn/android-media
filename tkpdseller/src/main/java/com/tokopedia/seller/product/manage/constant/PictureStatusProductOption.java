package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.seller.product.manage.constant.PictureStatusProductOption.NOT_USED;
import static com.tokopedia.seller.product.manage.constant.PictureStatusProductOption.WITHOUT_IMAGE;
import static com.tokopedia.seller.product.manage.constant.PictureStatusProductOption.WITH_IMAGE;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

@StringDef({WITH_IMAGE, WITHOUT_IMAGE, NOT_USED})
public @interface PictureStatusProductOption {
    String WITH_IMAGE = "1";
    String WITHOUT_IMAGE = "2";
    String NOT_USED = "-1";
}
