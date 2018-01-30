package com.tokopedia.session.activation.data.source;

import android.content.Context;

import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.activation.data.ChangeEmailModel;
import com.tokopedia.session.activation.data.mapper.ChangeEmailMapper;

import rx.Observable;

/**
 * Created by nisie on 4/18/17.
 */

public class CloudChangeEmailSource {

    private Context context;
    private final AccountsService accountsService;
    private ChangeEmailMapper changeEmailMapper;

    public CloudChangeEmailSource(Context context,
                                      AccountsService accountsService,
                                      ChangeEmailMapper changeEmailMapper) {
        this.context = context;
        this.accountsService = accountsService;
        this.changeEmailMapper = changeEmailMapper;
    }

    public Observable<ChangeEmailModel> changeEmail(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().changeEmail(AuthUtil.generateParamsNetwork2(context, params))
                .map(changeEmailMapper);
    }
}
