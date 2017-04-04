package com.tokopedia.seller.product.data.source.cache;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public class CategoryVersionCache {
    private static final String CATEGORY_HADES_VERSION = "STORED_CATEGORY_HADES_VERSION";
    private static final String VERSION_NUMBER = "VERSION_NUMBER";
    private static final int MINUTES = 60;
    private final Context context;

    public CategoryVersionCache(Context context) {
        this.context = context;
    }

    public Long getCategoryVersion() {
        return new LocalCacheHandler(context, CATEGORY_HADES_VERSION).getLong(VERSION_NUMBER);
    }

    public void storeVersion(Long apiVersion) {
        LocalCacheHandler localCache = new LocalCacheHandler(context, CATEGORY_HADES_VERSION);
        localCache.putLong(VERSION_NUMBER, apiVersion);
        localCache.applyEditor();
    }

    public boolean isNeedCategoryVersionCheck() {
        return new LocalCacheHandler(context, CATEGORY_HADES_VERSION).isExpired();
    }

    public void storeNeedCategoryVersionCheck(Integer interval) {
        new LocalCacheHandler(context, CATEGORY_HADES_VERSION).setExpire(interval * MINUTES);
    }

    public void clearCategoryTimer() {
        LocalCacheHandler.clearCache(context, CategoryDatabaseManager.KEY_STORAGE_NAME);
    }
}
