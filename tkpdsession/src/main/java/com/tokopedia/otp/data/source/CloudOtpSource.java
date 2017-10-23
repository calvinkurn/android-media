package com.tokopedia.otp.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.RequestOtpModel;
import com.tokopedia.otp.data.ValidateOtpModel;
import com.tokopedia.otp.data.mapper.OldRequestOtpMapper;
import com.tokopedia.otp.data.mapper.OldValidateOtpMapper;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */

public class CloudOtpSource {

    private final Context context;
    private AccountsService accountsService;
    private OldRequestOtpMapper oldRequestOtpMapper;
    private OldValidateOtpMapper validateOtpMapper;

    public CloudOtpSource(Context context, AccountsService accountsService,
                          OldRequestOtpMapper oldRequestOtpMapper, OldValidateOtpMapper validateOtpMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.oldRequestOtpMapper = oldRequestOtpMapper;
        this.validateOtpMapper = validateOtpMapper;
    }

    public Observable<RequestOtpModel> requestOtp(TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .requestOtp(SessionHandler.getLoginID(context), params).map(oldRequestOtpMapper);
    }

    public Observable<ValidateOtpModel> validateOtp(TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .validateOtp(params).map(validateOtpMapper);
    }
}
