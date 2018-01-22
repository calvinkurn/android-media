package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.tokocash.network.TkpdTokoCashResponse;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.InfoQrEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.QrPaymentEntity;

import java.util.HashMap;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class CloudQrPaymentDataSource implements QrPaymentDataSource {

    private static final String IDENTIFIER = "identifier";
    private WalletApi walletApi;

    public CloudQrPaymentDataSource(WalletApi walletApi) {
        this.walletApi = walletApi;
    }

    @Override
    public Observable<InfoQrEntity> getInfoQrTokoCash(HashMap<String, Object> mapParams) {
        return walletApi.getInfoQrTokoCash(mapParams.get(IDENTIFIER).toString())
                .flatMap(new Func1<Response<TkpdTokoCashResponse>, Observable<InfoQrEntity>>() {
                    @Override
                    public Observable<InfoQrEntity> call(Response<TkpdTokoCashResponse> response) {
                        return Observable.just(response.body().convertDataObj(InfoQrEntity.class));
                    }
                });
    }

    @Override
    public Observable<QrPaymentEntity> postQrPaymentTokoCash(HashMap<String, Object> mapParams) {
        return walletApi.postQrPaymentTokoCash(mapParams)
                .flatMap(new Func1<Response<TkpdTokoCashResponse>, Observable<QrPaymentEntity>>() {
                    @Override
                    public Observable<QrPaymentEntity> call(Response<TkpdTokoCashResponse> response) {
                        return Observable.just(response.body().convertDataObj(QrPaymentEntity.class));
                    }
                });
    }

    @Override
    public Observable<BalanceTokoCashEntity> getBalanceTokoCash(HashMap<String, Object> mapParams) {
        return walletApi.getBalanceTokoCash(mapParams)
                .flatMap(new Func1<Response<TkpdTokoCashResponse>, Observable<BalanceTokoCashEntity>>() {
                    @Override
                    public Observable<BalanceTokoCashEntity> call(Response<TkpdTokoCashResponse> response) {
                        return Observable.just(response.body().convertDataObj(BalanceTokoCashEntity.class));
                    }
                });
    }
}
