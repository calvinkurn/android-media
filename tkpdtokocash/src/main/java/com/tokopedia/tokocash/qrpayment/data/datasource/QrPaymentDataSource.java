package com.tokopedia.tokocash.qrpayment.data.datasource;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tokocash.qrpayment.data.entity.InfoQrEntity;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface QrPaymentDataSource {

    Observable<InfoQrEntity> getInfoQrTokoCash(TKPDMapParam<String, Object> mapParams);
}
