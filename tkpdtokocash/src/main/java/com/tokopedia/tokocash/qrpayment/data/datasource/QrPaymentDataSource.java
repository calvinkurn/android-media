package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tokocash.qrpayment.data.entity.InfoQrEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.QrPaymentEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.BalanceTokoCashEntity;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface QrPaymentDataSource {

    Observable<InfoQrEntity> getInfoQrTokoCash(TKPDMapParam<String, Object> mapParams);

    Observable<QrPaymentEntity> postQrPaymentTokoCash(TKPDMapParam<String, Object> mapParams);

    Observable<BalanceTokoCashEntity> getBalanceTokoCash(TKPDMapParam<String, Object> mapParams);
}
