package com.tokopedia.session.data.source;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class LocalDiscoverDataSource {

    public static final String KEY_DISCOVER = "KEY_DISCOVER";
    private static final String CACHE_EXPIRED = "Cache has expired";
    private final GlobalCacheManager globalCacheManager;

    public LocalDiscoverDataSource(GlobalCacheManager globalCacheManager) {
        this.globalCacheManager = globalCacheManager;
    }

    public Observable<DiscoverViewModel> getDiscover() {
        return Observable.just(KEY_DISCOVER)
                .map(new Func1<String, DiscoverViewModel>() {
                    @Override
                    public DiscoverViewModel call(String s) {
                        if (getCache() != null) {
                            return CacheUtil.convertStringToModel(getCache(),
                                    new TypeToken<ProfileModel>() {
                                    }.getType());
                        } else
                            throw new RuntimeException(CACHE_EXPIRED);
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

    private String getCache() {
        return globalCacheManager.getValueString(KEY_DISCOVER);
    }
}
