package com.tokopedia.session.data.source;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.EncoderDecoder;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsBasicService;
import com.tokopedia.session.domain.mapper.TokenMapper;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Iterator;

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
                        convert(params.getParameters()), GCMHandler.getRegistrationId(MainApplication.getAppContext()) ,sessionHandler.getLoginID() ))
                .map(tokenMapper)
                .doOnNext(saveAccessToken());

    }

    private TKPDMapParam<String, Object> convert(HashMap<String, Object> params){
        TKPDMapParam<String, Object> newParams = new TKPDMapParam<>();
        for(Iterator<String> key = params.keySet().iterator(); key.hasNext(); ){
            String next = key.next();
            newParams.put(next, params.get(next));
        }
        return newParams;
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
