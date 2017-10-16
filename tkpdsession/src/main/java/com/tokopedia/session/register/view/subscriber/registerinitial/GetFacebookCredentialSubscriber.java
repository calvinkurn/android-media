package com.tokopedia.session.register.view.subscriber.registerinitial;

import com.facebook.AccessToken;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;

/**
 * @author by nisie on 10/11/17.
 */

public class GetFacebookCredentialSubscriber {
    private final RegisterInitial.View viewListener;

    public GetFacebookCredentialSubscriber(RegisterInitial.View viewListener) {
        this.viewListener = viewListener;
    }

    public void onError(Exception e) {
        viewListener.dismissProgressBar();
        viewListener.onErrorGetFacebookCredential(ErrorHandler.getErrorMessage(e));
    }

    public void onSuccess(AccessToken accessToken) {
        viewListener.dismissProgressBar();
        viewListener.onSuccessGetFacebookCredential(accessToken);
    }
}
