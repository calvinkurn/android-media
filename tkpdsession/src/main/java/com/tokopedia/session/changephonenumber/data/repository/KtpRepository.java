package com.tokopedia.session.changephonenumber.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.CheckStatusModel;

import rx.Observable;

/**
 * Created by nisie on 3/10/17.
 */

public interface KtpRepository {

    Observable<CheckStatusModel> checkStatus(TKPDMapParam<String, Object> parameters);
}
