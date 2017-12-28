package com.tokopedia.session.changephonenumber.data.source;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.changephonenumber.data.mapper.GetWarningMapper;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * Created by milhamj on 27/12/17.
 */

public class CloudGetWarningSource {
    private final AccountsService accountsService;
    private GetWarningMapper getWarningMapper;

    @Inject
    public CloudGetWarningSource(@Named(BEARER_SERVICE) AccountsService accountsService,
                                 GetWarningMapper getWarningMapper) {
        this.accountsService = accountsService;
        this.getWarningMapper = getWarningMapper;
    }

    public Observable<WarningViewModel> getWarning(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().getWarning(params)
                .map(getWarningMapper);
    }
}
