package com.tokopedia.session.changephonenumber.data.source;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.mapper.GetWarningMapper;
import com.tokopedia.session.changephonenumber.data.mapper.SendEmailMapper;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * Created by milhamj on 28/12/17.
 */

public class CloudSendEmailSource {
    private final AccountsService accountsService;
    private SendEmailMapper sendEmailMapper;

    @Inject
    public CloudSendEmailSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                                SendEmailMapper sendEmailMapper) {
        this.accountsService = accountsService;
        this.sendEmailMapper = sendEmailMapper;
    }

    public Observable<Boolean> sendEmail(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().sendEmail(params)
                .map(sendEmailMapper);
    }
}
