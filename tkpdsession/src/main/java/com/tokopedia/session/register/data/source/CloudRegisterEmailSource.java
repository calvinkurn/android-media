package com.tokopedia.session.register.data.source;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.data.model.RegisterEmailModel;
import com.tokopedia.session.register.data.mapper.RegisterEmailMapper;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 4/13/17.
 */

public class CloudRegisterEmailSource {

    private Context context;
    private final AccountsService accountsService;
    private RegisterEmailMapper registerEmailMapper;
    private final SessionHandler sessionHandler;

    public CloudRegisterEmailSource(Context context,
                                    AccountsService accountsService,
                                    RegisterEmailMapper registerEmailMapper,
                                    SessionHandler sessionHandler) {
        this.context = context;
        this.accountsService = accountsService;
        this.registerEmailMapper = registerEmailMapper;
        this.sessionHandler = sessionHandler;
    }

    public Observable<RegisterEmailModel> registerEmail(TKPDMapParam<String, Object> params) {
        return accountsService.getApi().registerEmail(AuthUtil.generateParamsNetwork2(context, params))
                .map(registerEmailMapper)
                .doOnNext(saveUserId());
    }

    private Action1<RegisterEmailModel> saveUserId() {
        return new Action1<RegisterEmailModel>() {
            @Override
            public void call(RegisterEmailModel registerEmailModel) {
                if (registerEmailModel != null
                        && registerEmailModel.getRegisterEmailData() != null) {
                    sessionHandler.setLoginSession(false,
                            String.valueOf(registerEmailModel.getRegisterEmailData().getuId()),
                            null,
                            null,
                            false,
                            null);
                }
            }
        };
    }
}
