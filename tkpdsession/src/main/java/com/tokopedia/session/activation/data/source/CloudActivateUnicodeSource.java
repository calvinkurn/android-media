package com.tokopedia.session.activation.data.source;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.activation.data.ActivateUnicodeModel;
import com.tokopedia.session.activation.data.mapper.ActivateUnicodeMapper;

import rx.Observable;

/**
 * Created by nisie on 4/17/17.
 */

public class CloudActivateUnicodeSource {
    private Context context;
    private final AccountsService accountsService;
    private ActivateUnicodeMapper activateUnicodeMapper;

    public CloudActivateUnicodeSource(Context context,
                                      AccountsService accountsService,
                                      ActivateUnicodeMapper activateUnicodeMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.activateUnicodeMapper = activateUnicodeMapper;
    }

    public Observable<ActivateUnicodeModel> activateWithUnicode(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().activateWithUnicode(AuthUtil.generateParamsNetwork2(context, params))
                .map(activateUnicodeMapper);
    }
}
