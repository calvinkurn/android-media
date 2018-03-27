package com.tokopedia.session.data.source;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class LocalDiscoverDataSource {

    public static final String KEY_DISCOVER = "KEY_DISCOVER";
    private static final String CACHE_EXPIRED = "Cache has expired";
    private final GlobalCacheManager globalCacheManager;

    @Inject
    public LocalDiscoverDataSource(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<DiscoverViewModel> getDiscover(final String source) {
        return Observable.just(KEY_DISCOVER + source)
                .map(new Func1<String, DiscoverViewModel>() {
                    @Override
                    public DiscoverViewModel call(String s) {
                        if (getCache(source) != null) {
                            return CacheUtil.convertStringToModel(getCache(source),
                                    new TypeToken<DiscoverViewModel>() {
                                    }.getType());
                        } else {
                            throw new RuntimeException(CACHE_EXPIRED);
                        }
                    }
                })
                .first(new Func1<DiscoverViewModel, Boolean>() {
                    @Override
                    public Boolean call(DiscoverViewModel discoverViewModel) {
                        return !discoverViewModel.getProviders().isEmpty()
                                && !TextUtils.isEmpty(discoverViewModel.getUrlBackground());
                    }
                });
    }

    private String getCache(String source) {
        return globalCacheManager.getValueString(KEY_DISCOVER + source);
    }
}
