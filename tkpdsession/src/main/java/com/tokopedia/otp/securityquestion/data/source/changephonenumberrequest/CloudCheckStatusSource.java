package com.tokopedia.otp.securityquestion.data.source.changephonenumberrequest;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.CheckStatusModel;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.CheckStatusMapper;

import rx.Observable;

/**
 * Created by nisie on 3/10/17.
 */

public class CloudCheckStatusSource {
    private Context context;
    private final AccountsService accountsService;
    private CheckStatusMapper checkStatusMapper;

    public CloudCheckStatusSource(Context context,
                                      AccountsService accountsService,
                                      CheckStatusMapper checkStatusMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.checkStatusMapper = checkStatusMapper;
    }

    public Observable<CheckStatusModel> checkStatus(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().checkStatusKtp(params)
                .map(checkStatusMapper);
    }
}
