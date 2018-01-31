package com.tokopedia.tokocash.qrpayment.data.repository;

import com.tokopedia.tokocash.qrpayment.data.datasource.QrPaymentDataSourceFactory;
import com.tokopedia.tokocash.qrpayment.data.mapper.BalanceTokoCashMapper;
import com.tokopedia.tokocash.qrpayment.data.mapper.InfoQrMapper;
import com.tokopedia.tokocash.qrpayment.data.mapper.QrPaymentMapper;
import com.tokopedia.tokocash.qrpayment.domain.IQrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class QrPaymentRepository implements IQrPaymentRepository {

    private QrPaymentDataSourceFactory qrPaymentDataSourceFactory;

    @Inject
    public QrPaymentRepository(QrPaymentDataSourceFactory qrPaymentDataSourceFactory) {
        this.qrPaymentDataSourceFactory = qrPaymentDataSourceFactory;
    }

    @Override
    public Observable<InfoQrTokoCash> getInfoQrTokoCash(HashMap<String, Object> mapParams) {
        return qrPaymentDataSourceFactory.create().getInfoQrTokoCash(mapParams)
                .map(new InfoQrMapper());
    }

    @Override
    public Observable<QrPaymentTokoCash> postQrPayment(HashMap<String, Object> mapParams) {
        return qrPaymentDataSourceFactory.create().postQrPaymentTokoCash(mapParams)
                .map(new QrPaymentMapper());
    }

    @Override
    public Observable<BalanceTokoCash> getBalanceTokoCash(HashMap<String, Object> mapParams) {
        return qrPaymentDataSourceFactory.create().getBalanceTokoCash(mapParams)
                .map(new BalanceTokoCashMapper());
    }
}
