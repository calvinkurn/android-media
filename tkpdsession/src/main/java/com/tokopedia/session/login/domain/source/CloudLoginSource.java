package com.tokopedia.session.login.domain.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.login.domain.mapper.MakeLoginMapper;
import com.tokopedia.session.login.domain.model.MakeLoginDomainModel;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

public class CloudLoginSource {

    private final Context context;
    private final AccountsService accountsService;
    private final MakeLoginMapper makeLoginMapper;

    public CloudLoginSource(Context context,
                            AccountsService accountsService,
                            MakeLoginMapper makeLoginMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.makeLoginMapper = makeLoginMapper;
    }


    public Observable<MakeLoginDomainModel> makeLogin(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .makeLogin(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(makeLoginMapper);
    }
}
