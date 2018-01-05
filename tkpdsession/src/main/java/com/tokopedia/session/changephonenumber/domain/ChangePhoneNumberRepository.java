package com.tokopedia.session.changephonenumber.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import rx.Observable;


/**
 * Created by milhamj on 27/12/17.
 */

public interface ChangePhoneNumberRepository {

    Observable<WarningViewModel> getWarning(TKPDMapParam<String, Object> parameters);

    Observable<Boolean> sendEmailOTP(TKPDMapParam<String, Object> parameters);

    Observable<Boolean> validateNumber(TKPDMapParam<String, Object> parameters);

    Observable<Boolean> validateEmailCode(TKPDMapParam<String, Object> parameters);
}
