package com.tokopedia.session.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.session.domain.mapper.DiscoverMapper;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/10/17.
 */

public class CloudDiscoverDataSource {

    private final GlobalCacheManager globalCacheManager;
    AccountsService accountsService;
    DiscoverMapper discoverMapper;

    public CloudDiscoverDataSource(GlobalCacheManager globalCacheManager,
                                   AccountsService accountsService,
                                   DiscoverMapper discoverMapper) {
        this.globalCacheManager = globalCacheManager;
        this.accountsService = accountsService;
        this.discoverMapper = discoverMapper;
    }

    public Observable<DiscoverViewModel> getDiscover() {
        return accountsService.getApi()
                .discoverLogin()
                .map(discoverMapper)
                .doOnNext(saveToCache());
    }

    private Action1<DiscoverViewModel> saveToCache() {
        return new Action1<DiscoverViewModel>() {
            @Override
            public void call(DiscoverViewModel discoverViewModel) {
                if (discoverViewModel != null) {
                    globalCacheManager.setKey(LocalDiscoverDataSource.KEY_DISCOVER);
                    globalCacheManager.setValue(CacheUtil.convertModelToString(discoverViewModel,
                            new TypeToken<DiscoverViewModel>() {
                            }.getType()));
                    globalCacheManager.setCacheDuration(86400);
                    globalCacheManager.store();
                }
            }
        };
    }
}
