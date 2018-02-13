package com.tokopedia.session.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.data.source.GetTokenDataSource;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by stevenfredian on 3/8/17.
 */

public class GetTokenUseCase extends UseCase<TokenViewModel> {

    public static final String GRANT_TYPE = "grant_type";
    public static final String SOCIAL_TYPE = "social_type";
    public static final String ACCESS_TOKEN = "access_token";

    public static final int SOCIAL_TYPE_FACEBOOK = 1;
    public static final int SOCIAL_TYPE_GPLUS = 2;
    public static final int SOCIAL_TYPE_PHONE_NUMBER = 5;

    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirect_uri";

    public static final String GRANT_PASSWORD = "password";
    public static final String GRANT_SDK = "extension";
    public static final String GRANT_WEBVIEW = "authorization_code";

    private final GetTokenDataSource repository;

    @Inject
    public GetTokenUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           GetTokenDataSource repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    public Observable<TokenViewModel> createObservable(RequestParams requestParams) {
        return repository.getAccessToken(requestParams);
    }

    public static RequestParams getParamThirdParty(int socialType, String accessToken) {
        RequestParams params = RequestParams.create();
        params.putString(GRANT_TYPE, GRANT_SDK);
        params.putInt(SOCIAL_TYPE, socialType);
        params.putString(ACCESS_TOKEN, accessToken);
        return params;
    }

    public static RequestParams getParamRegisterWebview(String code, String redirectUri) {
        RequestParams params = RequestParams.create();
        params.putString(GRANT_TYPE, GRANT_WEBVIEW);
        params.putString(CODE, code);
        params.putString(REDIRECT_URI, redirectUri);
        return params;
    }

    public static RequestParams getParamLogin(String email, String password) {
        RequestParams params = RequestParams.create();
        params.putString(GRANT_TYPE, GRANT_PASSWORD);
        params.putString(PASSWORD, password);
        params.putString(USER_NAME, email);
        return params;
    }
}
