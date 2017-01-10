package com.tokopedia.session.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.session.base.BaseImpl;
import com.tokopedia.core.session.model.FacebookModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginProviderModel;

import java.util.List;

/**
 * Created by stevenfredian on 10/18/16.
 */

public abstract class RegisterInitialPresenter extends BaseImpl<RegisterInitialView> {

    public RegisterInitialPresenter(RegisterInitialView view) {
        super(view);
    }


    public abstract void startLoginWithGoogle(Context activity, String type, LoginGoogleModel loginGoogleModel);

    public abstract void loginWebView(Context activity, Bundle bundle);

    public abstract void loginFacebook(Context activity, FacebookModel facebookModel, String token);

    public abstract void downloadProviderLogin(Context context);

    public abstract void saveProvider(List<LoginProviderModel.ProvidersBean> listProvider);

    public abstract void storeCacheGTM(String registerType, String name);

    public abstract void setData(Context activity, int type, Bundle data);

    public abstract void unSubscribeFacade();
}
