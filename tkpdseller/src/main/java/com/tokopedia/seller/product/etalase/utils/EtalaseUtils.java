package com.tokopedia.seller.product.etalase.utils;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;

/**
 * Created by noiz354 on 5/18/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class EtalaseUtils {

    public static final String FETCH_DEP_PARENT = "fetch_dep_parent";
    public static final String FETCH_DEP_CHILD = "fetch_dep_child";
    public static final String FETCH_ETALASE = "fetch_etalase";

    /**
     * init cache if not null
     *
     * @param context
     * @param text
     * @return
     */
    public static LocalCacheHandler initCacheIfNotNull(Context context, String text) {
        switch (text) {
            case FETCH_DEP_PARENT:
            case FETCH_DEP_CHILD:
            case FETCH_ETALASE:
                return new LocalCacheHandler(context, text);
            default:
                return null;
        }
    }

    public static void clearEtalaseCache(Context context) {
        LocalCacheHandler fetchEtalaseTimer = initCacheIfNotNull(context, FETCH_ETALASE);
        fetchEtalaseTimer.setExpire(0);
    }

    public static void clearDepartementCache(Context context) {
        LocalCacheHandler.clearCache(context, EtalaseUtils.FETCH_DEP_CHILD);
        LocalCacheHandler.clearCache(context, EtalaseUtils.FETCH_DEP_PARENT);
    }
}
