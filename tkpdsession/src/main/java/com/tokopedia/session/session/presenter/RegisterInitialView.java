package com.tokopedia.session.session.presenter;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.session.model.LoginProviderModel;

import java.util.List;

/**
 * Created by stevenfredian on 10/18/16.
 */
@Deprecated
public interface RegisterInitialView extends BaseView{
    void showProvider(List<LoginProviderModel.ProvidersBean> providerList);

    boolean checkHasNoProvider();

    void addProgressBar();

    void removeProgressBar();

    void showProgress(boolean b);

    void showError(String string);

    void onMessageError(int discoverLogin, String s);

    void finishActivity();

    void moveToFragmentSecurityQuestion(int user_check_security_1, int user_check_security_2, int user_id);
}
