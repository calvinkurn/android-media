package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.network.api.WalletApi;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class BalanceDataSourceFactory {

    private CacheManager cacheManager;
    private ApolloClient apolloClient;

    @Inject
    public BalanceDataSourceFactory(ApolloClient apolloClient, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.apolloClient = apolloClient;
    }

    public BalanceDataSource createBalanceTokoCashDataSource() {
        return new CloudBalanceDataSource(cacheManager, apolloClient);
    }

    public BalanceDataSource createLocalBalanceTokoCashDataSource() {
        return new LocalBalanceDataSource(cacheManager);
    }
}
