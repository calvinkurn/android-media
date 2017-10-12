package com.tokopedia.session.register.data.factory;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.register.data.mapper.DiscoverMapper;
import com.tokopedia.session.register.data.source.CloudDiscoverDataSource;
import com.tokopedia.session.register.data.source.CloudTokenDataSource;
import com.tokopedia.session.register.data.source.LocalDiscoverDataSource;

/**
 * @author by nisie on 10/10/17.
 */

public class SessionFactory {

    private final GlobalCacheManager globalCacheManager;
    private final AccountsService accountsBasicService;
    private AccountsService accountsService;
    private DiscoverMapper discoverMapper;
    private TokenMapper tokenMapper;

    public SessionFactory(GlobalCacheManager globalCacheManager,
                          AccountsService accountsService,
                          AccountsService accountsBasicService,
                          DiscoverMapper discoverMapper,
                          TokenMapper tokenMapper) {
        this.globalCacheManager = globalCacheManager;
        this.accountsService = accountsService;
        this.accountsBasicService = accountsBasicService;
        this.discoverMapper = discoverMapper;
        this.tokenMapper = tokenMapper;
    }

    public CloudDiscoverDataSource createCloudDiscoverDataSource() {
        return new CloudDiscoverDataSource(globalCacheManager, accountsService, discoverMapper);
    }

    public LocalDiscoverDataSource createLocalDiscoverDataSource() {
        return new LocalDiscoverDataSource(globalCacheManager);
    }

    public CloudTokenDataSource createCloudTokenDataSource() {
        return new CloudTokenDataSource(accountsBasicService, tokenMapper);
    }

}
