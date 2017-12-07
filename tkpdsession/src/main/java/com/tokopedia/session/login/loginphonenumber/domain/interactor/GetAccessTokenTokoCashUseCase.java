package com.tokopedia.session.login.loginphonenumber.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.session.login.loginphonenumber.data.source.WalletTokenSource;
import com.tokopedia.session.login.loginphonenumber.domain.model.AccessTokenTokoCashDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/6/17.
 */

public class GetAccessTokenTokoCashUseCase extends UseCase<AccessTokenTokoCashDomain> {

    private static final String PARAM_GRANT_TYPE = "grant_type";
    private static final String TYPE_AUTHORIZATION_CODE = "authorization_code";
    private static final String PARAM_CODE = "code";
    private final WalletTokenSource tokoCashTokenSource;

    @Inject
    public GetAccessTokenTokoCashUseCase(WalletTokenSource tokoCashTokenSource) {
        this.tokoCashTokenSource = tokoCashTokenSource;
    }

    @Override
    public Observable<AccessTokenTokoCashDomain> createObservable(RequestParams requestParams) {
        return tokoCashTokenSource.getAccessToken(requestParams);
    }

    public static RequestParams getParam(String code) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_GRANT_TYPE, TYPE_AUTHORIZATION_CODE);
        params.putString(PARAM_CODE, code);
        return params;
    }
}
