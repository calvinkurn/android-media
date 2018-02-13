package com.tokopedia.session.register.data.source;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.data.model.RegisterEmailModel;
import com.tokopedia.session.register.data.mapper.RegisterEmailMapper;

import rx.Observable;

/**
 * Created by nisie on 4/13/17.
 */

public class CloudRegisterEmailSource {

    private Context context;
    private final AccountsService accountsService;
    private RegisterEmailMapper registerEmailMapper;

    public CloudRegisterEmailSource(Context context,
                                    AccountsService accountsService,
                                    RegisterEmailMapper registerEmailMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.registerEmailMapper = registerEmailMapper;
    }

    public Observable<RegisterEmailModel> registerEmail(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().registerEmail(AuthUtil.generateParamsNetwork2(context, params))
                .map(registerEmailMapper);
    }

}
