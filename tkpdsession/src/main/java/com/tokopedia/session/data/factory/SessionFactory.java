package com.tokopedia.session.data.factory;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.domain.mapper.DiscoverMapper;
import com.tokopedia.session.data.source.CloudDiscoverDataSource;
import com.tokopedia.session.data.source.CloudTokenDataSource;
import com.tokopedia.session.data.source.LocalDiscoverDataSource;

/**
 * @author by nisie on 10/10/17.
 */

public class SessionFactory {

    private final GlobalCacheManager globalCacheManager;
    private final AccountsService accountsBasicService;
    private final AccountsService accountsBearerService;
    private final SessionHandler sessionHandler;
    private AccountsService accountsService;
    private DiscoverMapper discoverMapper;
    private TokenMapper tokenMapper;

    public SessionFactory(GlobalCacheManager globalCacheManager,
                          SessionHandler sessionHandler,
                          AccountsService accountsService,
                          AccountsService accountsBasicService,
                          AccountsService accountsBearerService,
                          DiscoverMapper discoverMapper,
                          TokenMapper tokenMapper) {
        this.globalCacheManager = globalCacheManager;
        this.sessionHandler = sessionHandler;
        this.accountsService = accountsService;
        this.accountsBasicService = accountsBasicService;
        this.accountsBearerService = accountsBearerService;
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
        return new CloudTokenDataSource(accountsBasicService, tokenMapper, sessionHandler);
    }

}
