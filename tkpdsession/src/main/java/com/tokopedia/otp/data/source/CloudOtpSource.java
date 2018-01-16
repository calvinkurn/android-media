package com.tokopedia.otp.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.model.RequestOtpModel;
import com.tokopedia.otp.data.model.ValidateOtpModel;
import com.tokopedia.otp.data.mapper.RequestOtpMapper;
import com.tokopedia.otp.data.mapper.ValidateOtpMapper;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */
@Deprecated
public class CloudOtpSource {

    private final Context context;
    private AccountsService accountsService;
    private RequestOtpMapper requestOtpMapper;
    private ValidateOtpMapper validateOtpMapper;

    public CloudOtpSource(Context context, AccountsService accountsService,
                          RequestOtpMapper requestOtpMapper, ValidateOtpMapper validateOtpMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.requestOtpMapper = requestOtpMapper;
        this.validateOtpMapper = validateOtpMapper;
    }

    public Observable<RequestOtpModel> requestOtp(TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .requestOtp(SessionHandler.getLoginID(context), params).map(requestOtpMapper);
    }

    public Observable<ValidateOtpModel> validateOtp(TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .validateOtp(params).map(validateOtpMapper);
    }
}
