package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.apiservice.WalletService;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 2/15/18.
 */

public class BalanceDataSourceFactory {

    private WalletService walletService;
    private CacheManager cacheManager;

    @Inject
    public BalanceDataSourceFactory(WalletService walletService, CacheManager cacheManager) {
        this.walletService = walletService;
        this.cacheManager = cacheManager;
    }

    public BalanceDataSource createBalanceTokoCashDataSource() {
        return new CloudBalanceDataSource(walletService, cacheManager);
    }

    public BalanceDataSource createLocalBalanceTokoCashDataSource() {
        return new LocalBalanceDataSource(cacheManager);
    }
}
