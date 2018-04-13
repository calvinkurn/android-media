package com.tokopedia.session.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.EncoderDecoder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsBasicService;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * @author by nisie on 10/11/17.
 */

public class GetTokenDataSource {
    private final AccountsBasicService basicService;
    private final TokenMapper tokenMapper;
    private final SessionHandler sessionHandler;

    public GetTokenDataSource(AccountsBasicService basicService,
                              TokenMapper tokenMapper,
                              SessionHandler sessionHandler) {
        this.basicService = basicService;
        this.tokenMapper = tokenMapper;
        this.sessionHandler = sessionHandler;
    }

    public Observable<TokenViewModel> getAccessToken(RequestParams params) {
        return basicService.getApi()
                .getToken(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                        params.getParameters()))
                .map(tokenMapper)
                .doOnNext(saveAccessToken());

    }

    private Action1<TokenViewModel> saveAccessToken() {
        return new Action1<TokenViewModel>() {
            @Override
            public void call(TokenViewModel tokenModel) {
                sessionHandler.setToken(
                        tokenModel.getAccessToken(),
                        tokenModel.getTokenType(),
                        EncoderDecoder.Encrypt(tokenModel.getRefreshToken(),
                                SessionHandler.getRefreshTokenIV(getApplicationContext()))
                );
            }
        };
    }
}
