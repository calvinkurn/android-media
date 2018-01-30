package com.tokopedia.session.activation.data.source;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.activation.data.ResendActivationModel;
import com.tokopedia.session.activation.data.mapper.ResendActivationMapper;

import rx.Observable;

/**
 * Created by nisie on 4/17/17.
 */

public class CloudResendActivationSource {

    private Context context;
    private final AccountsService accountsService;
    private ResendActivationMapper resendActivationMapper;

    public CloudResendActivationSource(Context context,
                                  AccountsService accountsService,
                                       ResendActivationMapper resendActivationMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.resendActivationMapper = resendActivationMapper;
    }

    public Observable<ResendActivationModel> resendActivation(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().resentActivation(params)
                .map(resendActivationMapper);
    }
}
