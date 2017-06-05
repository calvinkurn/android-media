package com.tokopedia.otp.phoneverification.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.otp.phoneverification.data.ChangePhoneNumberModel;
import com.tokopedia.otp.phoneverification.data.mapper.ChangePhoneNumberMapper;

import rx.Observable;

/**
 * Created by nisie on 5/10/17.
 */

public class CloudChangeMsisdnSource {
    private final Context context;
    private final AccountsService accountsService;
    private final ChangePhoneNumberMapper changePhoneNumberMapper;

    public CloudChangeMsisdnSource(Context context,
                                   AccountsService accountsService,
                                   ChangePhoneNumberMapper changePhoneNumberMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.changePhoneNumberMapper = changePhoneNumberMapper;
    }


    public Observable<ChangePhoneNumberModel> changePhoneNumber(TKPDMapParam<String, Object> parameters) {
        return accountsService.getApi()
                .changePhoneNumber(AuthUtil.generateParamsNetwork2(context, parameters))
                .map(changePhoneNumberMapper);
    }
}
