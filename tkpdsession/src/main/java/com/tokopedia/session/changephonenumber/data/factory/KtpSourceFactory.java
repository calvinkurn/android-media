package com.tokopedia.session.changephonenumber.data.factory;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.changephonenumber.data.mapper.changephonenumberrequest.CheckStatusMapper;
import com.tokopedia.session.changephonenumber.data.source.changephonenumberrequest.CloudCheckStatusSource;

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
