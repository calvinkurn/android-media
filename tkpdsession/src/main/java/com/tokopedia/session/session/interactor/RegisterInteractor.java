package com.tokopedia.session.session.interactor;

import android.content.Context;

import com.tokopedia.core.session.model.LoginProviderModel;

import java.util.Map;

/**
 * Created by stevenfredian on 6/21/16.
 */
@Deprecated
public interface RegisterInteractor {

    void downloadProvider(Context context, DiscoverLoginListener listener);

    void unSubscribe();

    void validateEmail(Context context, Map<String, String> params, ValidateEmailListener validateEmailListener);

    interface DiscoverLoginListener{
        void onSuccess(LoginProviderModel result);

        void onError(String s);

        void onTimeout();

        void onThrowable(Throwable e);
    }

    interface ValidateEmailListener{
        void onSuccess(boolean isActive);

        void onError(String s);

        void onTimeout();

        void onThrowable(Throwable e);
    }
}
