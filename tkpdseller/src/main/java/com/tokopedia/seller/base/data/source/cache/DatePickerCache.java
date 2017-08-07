package com.tokopedia.seller.base.data.source.cache;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.utils.ErrorCheck;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.seller.base.data.source.cache.model.DatePickerCacheModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/27/17.
 */
public class DatePickerCache {

    private static final int CACHE_TIMEOUT = 24 * 3600; // 1DAY
    private static final String DATE_PICKER_SETTING = "DATE_PICKER_SETTING";

    private final GlobalCacheManager cacheManager;

    @Inject
    public DatePickerCache(GlobalCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Observable<DatePickerCacheModel> getDatePickerSetting() {
        return getDatePickerCache().map(new ErrorCheck<DatePickerCacheModel>());
    }

    @NonNull
    private Observable<DatePickerCacheModel> getDatePickerCache() {
        return Observable.just(true)
                .map(new Func1<Boolean, DatePickerCacheModel>() {
                         @Override
                         public DatePickerCacheModel call(Boolean aBoolean) {
                             return cacheManager.getConvertObjData(
                                     DATE_PICKER_SETTING,
                                     DatePickerCacheModel.class
                             );
                         }
                     }
                );
    }

    public Observable<Boolean> storeDatePickerSetting(final DatePickerCacheModel datePickerCacheModel) {
        return Observable.just(datePickerCacheModel).map(new Func1<DatePickerCacheModel, Boolean>() {
            @Override
            public Boolean call(DatePickerCacheModel datePickerCacheModel) {
                String stringData = CacheUtil.convertModelToString(
                        datePickerCacheModel,
                        new TypeToken<DatePickerCacheModel>() {
                        }.getType()
                );
                saveToCache(DATE_PICKER_SETTING, stringData);
                return true;
            }
        });
    }

    private void saveToCache(String key, String stringData) {
        cacheManager.setKey(key)
                    .setValue(stringData)
                    .setCacheDuration(CACHE_TIMEOUT)
                    .store();
    }

    public Observable<Boolean> clearCache() {
        cacheManager.delete(DATE_PICKER_SETTING);
        return Observable.just(true);
    }
}