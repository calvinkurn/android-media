package com.tokopedia.seller.product.manage.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.product.manage.constant.PictureStatusProductOption.WITH_AND_WITHOUT;
import static com.tokopedia.seller.product.manage.constant.PictureStatusProductOption.WITHOUT_IMAGE;
import static com.tokopedia.seller.product.manage.constant.PictureStatusProductOption.WITH_IMAGE;

/**
 * Created by zulfikarrahman on 9/22/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({WITH_IMAGE, WITHOUT_IMAGE, WITH_AND_WITHOUT})
public @interface PictureStatusProductOption {
    String WITH_IMAGE = "1";
    String WITHOUT_IMAGE = "2";
    String WITH_AND_WITHOUT = "-1";
}
