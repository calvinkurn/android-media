package com.tokopedia.session.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;
import com.tokopedia.session.register.data.repository.SessionRepository;

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

    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL_ACCOUNTS = "email";
    public static final String PICTURE_ACCOUNTS = "picture";
    public static final String FULL_NAME = "full_name";
    public static final String BIRTHDATE = "birthdate";
    public static final String GENDER_ACCOUNTS = "gender";
    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirect_uri";
    public static final String MSISDN = "msisdn";

    public static final String GRANT_PASSWORD = "password";
    public static final String GRANT_SDK = "extension";
    public static final String GRANT_WEBVIEW = "authorization_code";

    private final SessionRepository repository;

    public GetTokenUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                           SessionRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<TokenViewModel> createObservable(RequestParams requestParams) {
        return repository.getAccessToken(requestParams);
    }
//
//    private RequestParams getLoginParam(Context context, AccountsParameter parameter) {
//        RequestParams params = RequestParams.create();
//        params.putString(GetTokenUseCase.GRANT_TYPE, parameter.getGrantType());
//
//        switch (parameter.getGrantType()) {
//            case Login.GRANT_PASSWORD:
//                params.putString(GetTokenUseCase.USER_NAME, parameter.getEmail());
//                params.putString(GetTokenUseCase.PASSWORD, parameter.getPassword());
//                break;
//            case Login.GRANT_SDK:
//                Parcelable parcelable = parameter.getParcelable();
//                params.putString(GetTokenUseCase.SOCIAL_TYPE, String.valueOf(parameter.getSocialType()));
//                if (parcelable instanceof GoogleSignInAccount) {
//                    GoogleSignInAccount account = (GoogleSignInAccount) parcelable;
//                    params.putString(GetTokenUseCase.SOCIAL_ID, account.getId());
//                    params.putString(GetTokenUseCase.EMAIL_ACCOUNTS, account.getEmail());
//                    params.putString(GetTokenUseCase.PICTURE_ACCOUNTS, String.valueOf(account.getPhotoUrl()));
//                    params.putString(GetTokenUseCase.FULL_NAME, account.getDisplayName());
//                } else if (parcelable instanceof FacebookModel) {
//                    FacebookModel account = (FacebookModel) parcelable;
//                    params.putString(GetTokenUseCase.SOCIAL_ID, account.getId());
//                    params.putString(GetTokenUseCase.EMAIL_ACCOUNTS, account.getEmail());
//                    params.putString(GetTokenUseCase.FULL_NAME, account.getName());
//                    params.putString(GetTokenUseCase.BIRTHDATE, account.getBirthday());
//                    params.putString(GetTokenUseCase.GENDER_ACCOUNTS, account.getGender());
//                }
//                break;
//            case Login.GRANT_WEBVIEW:
//                params.putString(GetTokenUseCase.CODE, parameter.getCode());
//                params.putString(GetTokenUseCase.REDIRECT_URI, parameter.getRedirectUri());
//                break;
//            default:
//                throw new RuntimeException("Invalid Observable to get Token");
//        }
//
//        return params;
//    }

    public static RequestParams getParamRegisterThirdParty(int socialType, String accessToken) {
        RequestParams params = RequestParams.create();
        params.putString(GRANT_TYPE, GRANT_SDK);
        params.putInt(SOCIAL_TYPE, socialType);
        params.putString(ACCESS_TOKEN, accessToken);
        return params;
    }
}
