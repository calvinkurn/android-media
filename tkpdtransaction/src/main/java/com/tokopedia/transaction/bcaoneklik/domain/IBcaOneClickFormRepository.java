package com.tokopedia.transaction.bcaoneklik.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickSuccessRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;

import rx.Observable;

/**
 * Created by kris on 7/25/17. Tokopedia
 */

public interface IBcaOneClickFormRepository {

    Observable<BcaOneClickData> getBcaOneClickAccessToken(
            TKPDMapParam<String, String> bcaOneClickParam
    );

    Observable<PaymentListModel> getPaymentListUserData(
            TKPDMapParam<String, String> oneClickListParam
    );

    Observable<PaymentListModel> deleteUserData(
            TKPDMapParam<String, String> oneClickListParam
    );

    Observable<BcaOneClickSuccessRegisterData> registerBcaOneClickData(
            TKPDMapParam<String, String> bcaOneClickRegisterParam
    );

    Observable<PaymentListModel> editBcaOneClickData(
            TKPDMapParam<String, String> bcaOneClickRegisterParam
    );

}
