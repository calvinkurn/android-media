package com.tokopedia.otp.securityquestion.data.source.changephonenumberrequest;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.UploadHostModel;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.GetUploadHostMapper;

import rx.Observable;

/**
 * Created by nisie on 3/9/17.
 */

public class CloudGetUploadHostSource {

    private Context context;
    private final AccountsService accountsService;
    private GetUploadHostMapper getUploadHostMapper;

    public CloudGetUploadHostSource(Context context,
                                    AccountsService accountsService,
                                    GetUploadHostMapper getUploadHostMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.getUploadHostMapper = getUploadHostMapper;
    }

    public Observable<UploadHostModel> getUploadHost(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().getUploadHost(params)
                .map(getUploadHostMapper);
    }

}
