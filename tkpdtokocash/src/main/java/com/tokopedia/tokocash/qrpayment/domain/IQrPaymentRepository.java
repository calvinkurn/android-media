package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface IQrPaymentRepository {

    Observable<InfoQrTokoCash> getInfoQrTokoCash(TKPDMapParam<String, Object> mapParams);

    Observable<QrPaymentTokoCash> postQrPayment(TKPDMapParam<String, Object> mapParams);

    Observable<BalanceTokoCash> getBalanceTokoCash(TKPDMapParam<String, Object> mapParams);
}
