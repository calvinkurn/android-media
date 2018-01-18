package com.tokopedia.otp.securityquestion.data.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.otp.securityquestion.domain.mapper.changephonenumberrequest.CheckStatusMapper;
import com.tokopedia.otp.securityquestion.data.source.changephonenumberrequest.CloudCheckStatusSource;

/**
 * Created by nisie on 3/10/17.
 */

public class KtpSourceFactory {
    private Context context;
    private final AccountsService accountsService;
    private CheckStatusMapper checkStatusMapper;

    public KtpSourceFactory(Context context,
                                    AccountsService accountsService,
                                    CheckStatusMapper checkStatusMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.checkStatusMapper = checkStatusMapper;
    }

    public CloudCheckStatusSource createCloudUploadHostDataStore() {
        return new CloudCheckStatusSource(context, accountsService, checkStatusMapper);
    }

}
