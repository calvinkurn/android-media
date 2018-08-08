package com.tokopedia.session.data.source;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.domain.mapper.DiscoverMapper;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by nisie on 10/10/17.
 */

public class CloudDiscoverDataSource {

    private final GlobalCacheManager globalCacheManager;
    private AccountsService accountsService;
    private DiscoverMapper discoverMapper;

    public CloudDiscoverDataSource(GlobalCacheManager globalCacheManager,
                                   AccountsService accountsService,
                                   DiscoverMapper discoverMapper) {
        this.globalCacheManager = globalCacheManager;
        this.accountsService = accountsService;
        this.discoverMapper = discoverMapper;
    }

    public Observable<DiscoverViewModel> getDiscover(com.tokopedia.usecase.RequestParams params) {
        return accountsService.getApi()
                .discoverLogin(params.getParameters())
                .map(discoverMapper)
                .doOnNext(saveToCache(params.getString(DiscoverUseCase.PARAM_TYPE, "")));
    }

    private Action1<DiscoverViewModel> saveToCache(final String source) {
        return new Action1<DiscoverViewModel>() {
            @Override
            public void call(DiscoverViewModel discoverViewModel) {
                if (discoverViewModel != null) {
                    globalCacheManager.setKey(LocalDiscoverDataSource.KEY_DISCOVER + source);
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
