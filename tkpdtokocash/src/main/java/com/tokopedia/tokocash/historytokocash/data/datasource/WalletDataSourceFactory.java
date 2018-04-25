package com.tokopedia.tokocash.historytokocash.data.datasource;

import com.google.gson.Gson;
import com.tokopedia.tokocash.network.api.WalletApi;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 12/18/17.
 */

public class WalletDataSourceFactory {

    private WalletApi walletApi;
    private Gson gson;

    @Inject
    public WalletDataSourceFactory(WalletApi walletApi, Gson gson) {
        this.walletApi = walletApi;
        this.gson = gson;
    }

    private WalletDataSource createCloudWalletDataSource() {
        return new CloudWalletDataSource(walletApi, gson);
    }

    public WalletDataSource create() {
        return createCloudWalletDataSource();
    }
}
