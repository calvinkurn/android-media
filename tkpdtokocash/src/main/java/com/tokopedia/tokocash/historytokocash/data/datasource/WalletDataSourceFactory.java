package com.tokopedia.tokocash.historytokocash.data.datasource;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class WalletDataSourceFactory {

    private WalletService walletService;

    public WalletDataSourceFactory(WalletService walletService) {
        this.walletService = walletService;
    }

    public WalletDataSource createCloudWalletDataSource() {
        return new CloudWalletDataSource(walletService);
    }

    public WalletDataSource create() {
        return createCloudWalletDataSource();
    }
}
