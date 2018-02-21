package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.tokocash.apiservice.WalletService;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class QrPaymentDataSourceFactory {

    private WalletService walletService;

    @Inject
    public QrPaymentDataSourceFactory(WalletService walletService) {
        this.walletService = walletService;
    }

    private QrPaymentDataSource createQrPaymentDataSource() {
        return new CloudQrPaymentDataSource(walletService);
    }

    public QrPaymentDataSource create() {
        return createQrPaymentDataSource();
    }
}
