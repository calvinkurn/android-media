package com.tokopedia.session.register.data.source;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.register.data.mapper.CheckMsisdnMapper;
import com.tokopedia.session.register.domain.model.CheckMsisdnDomain;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckMsisdnSource {
    private final AccountsService accountsService;
    private final CheckMsisdnMapper checkMsisdnMapper;

    public CheckMsisdnSource(AccountsService accountsService,
                             CheckMsisdnMapper checkMsisdnMapper) {
        this.accountsService = accountsService;
        this.checkMsisdnMapper = checkMsisdnMapper;
    }

    public Observable<CheckMsisdnDomain> changePhoneNumber(Context context, TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .checkMsisdnRegisterPhoneNumber(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(checkMsisdnMapper);
    }
}