package com.tokopedia.session.changephonenumber.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.CheckStatusModel;
import com.tokopedia.session.changephonenumber.data.mapper.CheckStatusMapper;
import com.tokopedia.session.changephonenumber.data.mapper.WarningMapper;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * Created by milhamj on 27/12/17.
 */

public class CloudChangePhoneNumberWarningSource {
    private final AccountsService accountsService;
    private WarningMapper warningMapper;

    @Inject
    public CloudChangePhoneNumberWarningSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                                               WarningMapper warningMapper) {
        this.accountsService = accountsService;
        this.warningMapper = warningMapper;
    }

    public Observable<WarningViewModel> getWarning(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().getWarning(params)
                .map(warningMapper);
    }
}
