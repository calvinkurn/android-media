package com.tokopedia.transaction.purchase.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by kris on 9/13/17. Tokopedia
 */

public interface ITxVerificationRepository {

    Observable<String> processCancelDialogResponse(TKPDMapParam<String, String> showCancelDialogParam);

    Observable<String> processCancelTransaction(TKPDMapParam<String, String> cancellationParam);

}
