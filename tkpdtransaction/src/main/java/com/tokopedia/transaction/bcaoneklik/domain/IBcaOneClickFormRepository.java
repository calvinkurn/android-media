package com.tokopedia.transaction.bcaoneklik.domain;

import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickRegisterData;
import com.tokopedia.transaction.bcaoneklik.model.BcaOneClickSuccessRegisterData;

import rx.Observable;

/**
 * Created by kris on 7/25/17. Tokopedia
 */

public interface IBcaOneClickFormRepository {

    Observable<BcaOneClickData> getBcaOneClickAccessToken(
            TKPDMapParam<String, String> bcaOneClickParam
    );

    Observable<BcaOneClickSuccessRegisterData> registerBcaOneClickData(
            TKPDMapParam<String, String> bcaOneClickRegisterParam
    );

}
