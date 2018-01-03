package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tokocash.apiservice.WalletService;
import com.tokopedia.tokocash.qrpayment.data.entity.InfoQrEntity;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class CloudQrPaymentDataSource implements QrPaymentDataSource {

    private static final String IDENTIFIER = "identifier";

    private WalletService walletService;

    public CloudQrPaymentDataSource(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public Observable<InfoQrEntity> getInfoQrTokoCash(TKPDMapParam<String, Object> mapParams) {
        return walletService.getApi().getInfoQrTokoCash(mapParams.get(IDENTIFIER).toString())
                .flatMap(new Func1<Response<TkpdDigitalResponse>, Observable<InfoQrEntity>>() {
                    @Override
                    public Observable<InfoQrEntity> call(Response<TkpdDigitalResponse> response) {
                        return Observable.just(response.body().convertDataObj(InfoQrEntity.class));
                    }
                });
    }
}
