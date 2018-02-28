package com.tokopedia.session.register.domain.interactor.registerinitial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.core.var.FacebookContainer;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * @author by nisie on 10/11/17.
 */

public class GetFacebookCredentialUseCase {

    private static final String PARAM_CALLBACK_MANAGER = "PARAM_CALLBACK_MANAGER";
    private static final String PARAM_FRAGMENT = "PARAM_FRAGMENT";

    @Inject
    public GetFacebookCredentialUseCase() {
    }

    public void execute(RequestParams requestParams, GetFacebookCredentialSubscriber subscriber) {
        Fragment fragment = (Fragment) requestParams.getObject(PARAM_FRAGMENT);
        CallbackManager callbackManager = (CallbackManager) requestParams.getObject(PARAM_CALLBACK_MANAGER);
        promptFacebookLogin(fragment, callbackManager, subscriber);

    }

    private void promptFacebookLogin(Fragment fragment, CallbackManager callbackManager,
                                     final GetFacebookCredentialSubscriber subscriber) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, FacebookContainer.readPermissions);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (TextUtils.isEmpty(loginResult.getAccessToken().getToken())) {
                    LoginManager.getInstance().logOut();
                    subscriber.onError(new ErrorMessageException(
                            MainApplication.getAppContext().getString(R.string.facebook_error_not_authorized),
                            ErrorCode.EMPTY_ACCESS_TOKEN));
                } else {
                    getFacebookEmail(loginResult.getAccessToken(), subscriber);
                }
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException e) {
                LoginManager.getInstance().logOut();
                if (e instanceof FacebookAuthorizationException) {
                    subscriber.onError(new ErrorMessageException(
                            MainApplication.getAppContext().getString(R.string.facebook_error_not_authorized),
                            ErrorCode.FACEBOOK_AUTHORIZATION_EXCEPTION));
                } else {
                    subscriber.onError(new ErrorMessageException(
                            MainApplication.getAppContext().getString(R.string.facebook_error_not_authorized),
                            ErrorCode.FACEBOOK_EXCEPTION));
                }
            }
        });

    }

    private void getFacebookEmail(final AccessToken accessToken, final GetFacebookCredentialSubscriber subscriber) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = object.getString("email");
                            subscriber.onSuccess(accessToken, email);

                        } catch (JSONException e) {
                            LoginManager.getInstance().logOut();
                            subscriber.onError(new ErrorMessageException(
                                    MainApplication.getAppContext().getString(R.string.facebook_error_not_authorized),
                                    ErrorCode.FACEBOOK_EXCEPTION));
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public static RequestParams getParam(Fragment fragment, CallbackManager callbackManager) {
        RequestParams params = RequestParams.create();
        params.putObject(PARAM_FRAGMENT, fragment);
        params.putObject(PARAM_CALLBACK_MANAGER, callbackManager);
        return params;
    }
}
