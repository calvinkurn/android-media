package com.tokopedia.session.register.data.source;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.register.data.mapper.RegisterPhoneNumberMapper;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public class CloudRegisterPhoneNumberSource {
    private Context context;
    private final AccountsService accountsService;
    private RegisterPhoneNumberMapper registerPhoneNumberMapper;

    public CloudRegisterPhoneNumberSource(Context context,
                                          AccountsService accountsService,
                                          RegisterPhoneNumberMapper registerPhoneNumberMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.registerPhoneNumberMapper = registerPhoneNumberMapper;
    }

    public Observable<RegisterPhoneNumberModel> registerPhoneNumber(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().registerPhoneNumber(AuthUtil.generateParamsNetwork2(context, params))
                .map(registerPhoneNumberMapper);
    }

}
