package com.tokopedia.session.register.data.source;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.EncoderDecoder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.register.data.mapper.RegisterPhoneNumberMapper;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by yfsx on 28/02/18.
 */

public class CloudRegisterPhoneNumberSource {
    private Context context;
    private final AccountsService accountsService;
    private RegisterPhoneNumberMapper registerPhoneNumberMapper;
    private SessionHandler sessionHandler;

    public CloudRegisterPhoneNumberSource(Context context,
                                          AccountsService accountsService,
                                          RegisterPhoneNumberMapper registerPhoneNumberMapper,
                                          SessionHandler sessionHandler) {
        this.context = context;
        this.accountsService = accountsService;
        this.registerPhoneNumberMapper = registerPhoneNumberMapper;
        this.sessionHandler = sessionHandler;
    }

    public Observable<RegisterPhoneNumberModel> registerPhoneNumber(
            Context context, TKPDMapParam<String, Object> params) {
        return accountsService.getApi()
                .registerPhoneNumber(AuthUtil.generateParamsNetwork2(context, params))
                .map(registerPhoneNumberMapper)
                .doOnNext(saveAccessToken());
    }

    private Action1<RegisterPhoneNumberModel> saveAccessToken() {
        return new Action1<RegisterPhoneNumberModel>() {
            @Override
            public void call(RegisterPhoneNumberModel registerPhoneNumberModel) {
                TokenViewModel tokenModel =
                        registerPhoneNumberModel.getRegisterPhoneNumberData().getTokenModel();
                sessionHandler.setToken(
                        tokenModel.getAccessToken(),
                        tokenModel.getTokenType(),
                        EncoderDecoder.Encrypt(tokenModel.getRefreshToken(),
                                SessionHandler.getRefreshTokenIV(context.getApplicationContext()))
                );
            }
        };
    }

}
