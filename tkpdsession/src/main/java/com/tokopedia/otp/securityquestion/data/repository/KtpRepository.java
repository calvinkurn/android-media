package com.tokopedia.otp.securityquestion.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.CheckStatusModel;

import rx.Observable;

/**
 * Created by nisie on 3/10/17.
 */

public interface KtpRepository {

    Observable<CheckStatusModel> checkStatus(TKPDMapParam<String, Object> parameters);
}
