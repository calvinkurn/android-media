package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.abstraction.common.data.model.storage.GlobalCacheManager;
import com.tokopedia.tokocash.network.api.WalletApi;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/26/18.
 */

public class TokoCashBalanceDataSourceFactory {

    private WalletApi walletApi;
    private GlobalCacheManager globalCacheManager;

    @Inject
    public TokoCashBalanceDataSourceFactory(WalletApi walletApi, GlobalCacheManager globalCacheManager) {
        this.walletApi = walletApi;
        this.globalCacheManager = globalCacheManager;
    }

    public TokoCashBalanceDataSource createBalanceTokoCashDataSource() {
        return new TokoCashBalanceCloudDataSource(walletApi, globalCacheManager);
    }

    public TokoCashBalanceDataSource createLocalBalanceTokoCashDataSource() {
        return new TokoCashBalanceLocalDataSource(globalCacheManager);
    }
}
