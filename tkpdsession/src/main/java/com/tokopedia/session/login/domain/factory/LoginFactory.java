package com.tokopedia.session.login.domain.factory;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.login.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.login.domain.model.MakeLoginDomainModel;
import com.tokopedia.session.login.domain.source.CloudLoginSource;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

public class LoginFactory {
    private final Context context;
    private final AccountsService accountsService;
    private final MakeLoginMapper makeLoginMapper;

    public LoginFactory(Context context,
                        AccountsService accountsService,
                        MakeLoginMapper makeLoginMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.makeLoginMapper = makeLoginMapper;
    }


    public Observable<MakeLoginDomainModel> createCloudLoginSource(TKPDMapParam<String, Object> parameters) {
        return new CloudLoginSource(context,accountsService, makeLoginMapper).makeLogin(parameters);
    }
}
