package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.network.api.WalletApi;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class BalanceDataSourceFactory {

    private WalletApi walletApi;
    private CacheManager cacheManager;

    @Inject
    public BalanceDataSourceFactory(WalletApi walletApi, CacheManager cacheManager) {
        this.walletApi = walletApi;
        this.cacheManager = cacheManager;
    }

    public BalanceDataSource createBalanceTokoCashDataSource() {
        return new CloudBalanceDataSource(walletApi, cacheManager);
    }

    public BalanceDataSource createLocalBalanceTokoCashDataSource() {
        return new LocalBalanceDataSource(cacheManager);
    }
}
