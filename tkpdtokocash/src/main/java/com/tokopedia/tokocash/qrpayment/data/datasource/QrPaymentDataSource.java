package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.tokocash.qrpayment.data.entity.InfoQrEntity;
import com.tokopedia.tokocash.qrpayment.data.entity.QrPaymentEntity;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface QrPaymentDataSource {

    Observable<InfoQrEntity> getInfoQrTokoCash(HashMap<String, Object> mapParams);

    Observable<QrPaymentEntity> postQrPaymentTokoCash(HashMap<String, Object> mapParams);
}
