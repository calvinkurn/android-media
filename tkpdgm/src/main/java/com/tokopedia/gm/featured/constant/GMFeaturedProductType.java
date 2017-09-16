package com.tokopedia.gm.featured.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.gm.featured.constant.GMFeaturedProductType.ARRANGE_DISPLAY;
import static com.tokopedia.gm.featured.constant.GMFeaturedProductType.DEFAULT_DISPLAY;
import static com.tokopedia.gm.featured.constant.GMFeaturedProductType.DELETE_DISPLAY;

/**
 * Created by normansyahputa on 9/12/17.
 */


@IntDef({DEFAULT_DISPLAY, ARRANGE_DISPLAY, DELETE_DISPLAY})
@Retention(RetentionPolicy.SOURCE)
public @interface GMFeaturedProductType {
    int DEFAULT_DISPLAY = 0;
    int ARRANGE_DISPLAY = 1;
    int DELETE_DISPLAY = 2;
}
