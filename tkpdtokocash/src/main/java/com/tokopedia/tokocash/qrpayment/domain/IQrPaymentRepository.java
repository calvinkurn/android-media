package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public interface IQrPaymentRepository {

    Observable<InfoQrTokoCash> getInfoQrTokoCash(TKPDMapParam<String, Object> mapParams);
}
