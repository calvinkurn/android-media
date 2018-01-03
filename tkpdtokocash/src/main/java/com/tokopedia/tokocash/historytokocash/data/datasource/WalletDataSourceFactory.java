package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.tokopedia.tokocash.apiservice.WalletService;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class WalletDataSourceFactory {

    private WalletService walletService;

    @Inject
    public WalletDataSourceFactory(WalletService walletService) {
        this.walletService = walletService;
    }

    private WalletDataSource createCloudWalletDataSource() {
        return new CloudWalletDataSource(walletService);
    }

    public WalletDataSource create() {
        return createCloudWalletDataSource();
    }
}
