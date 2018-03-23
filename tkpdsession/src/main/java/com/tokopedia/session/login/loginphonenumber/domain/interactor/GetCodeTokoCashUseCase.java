package com.tokopedia.session.login.loginphonenumber.domain.interactor;

import com.tokopedia.session.login.loginphonenumber.data.source.TokoCashCodeSource;
import com.tokopedia.session.login.loginphonenumber.domain.model.CodeTokoCashDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/5/17.
 */

public class GetCodeTokoCashUseCase extends UseCase<CodeTokoCashDomain> {

    public static final String PARAM_KEY = "key";
    public static final String PARAM_EMAIL = "email";
    private final TokoCashCodeSource tokoCashCodeSource;

    @Inject
    public GetCodeTokoCashUseCase(TokoCashCodeSource tokoCashCodeSource) {
        this.tokoCashCodeSource = tokoCashCodeSource;
    }

    @Override
    public Observable<CodeTokoCashDomain> createObservable(RequestParams requestParams) {
        return tokoCashCodeSource.getAccessToken(requestParams.getParameters());
    }

    public static RequestParams getParam(String key, String email) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_KEY, key);
        params.putString(PARAM_EMAIL, email);
        return params;
    }
}
