package com.tokopedia.seller.goldmerchant.featured.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.seller.goldmerchant.featured.constant.FeaturedProductType.ARRANGE_DISPLAY;
import static com.tokopedia.seller.goldmerchant.featured.constant.FeaturedProductType.DEFAULT_DISPLAY;
import static com.tokopedia.seller.goldmerchant.featured.constant.FeaturedProductType.DELETE_DISPLAY;

/**
 * Created by normansyahputa on 9/12/17.
 */


@IntDef({DEFAULT_DISPLAY, ARRANGE_DISPLAY, DELETE_DISPLAY})
@Retention(RetentionPolicy.SOURCE)
public @interface FeaturedProductType {
    int DEFAULT_DISPLAY = 0;
    int ARRANGE_DISPLAY = 1;
    int DELETE_DISPLAY = 2;
}
