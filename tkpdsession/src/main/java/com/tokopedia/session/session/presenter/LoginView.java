package com.tokopedia.session.session.presenter;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.session.session.fragment.LoginFragment;
import com.tokopedia.core.session.model.LoginProviderModel;

import java.util.List;

/**
 * Created by m.normansyah on 04/11/2015.
 */
public interface LoginView extends BaseView {
    String TAG = "MNORMANSYAH";
    String TEST_INT_KEY = "O";
    String messageTAG = "LoginView : ";

    void setAutoCompleteAdapter(List<String> LoginIdList);

    void setEmailText(String text);

    void setListener();

    void showProgress(boolean isShow);

    LoginFragment.FocusPair validateSignIn();

    void moveToFragmentSecurityQuestion(int security1, int security2, int userId, String email);

    void notifyAutoCompleteAdapter();

    void showDialog(String dialogText);

    void startLoginWithGoogle(String LoginType, Object model);

    void destroyActivity();

    void showProvider(List<LoginProviderModel.ProvidersBean> data);

    void hideProvider();

    void addProgressbar();

    void removeProgressBar();

    Context getContext();

    Activity getActivity();

    boolean checkHasNoProvider();

    void showError(String string);

    void triggerSaveAccount();

    void setSmartLock(int rcRead);

    void setSmartLock(int rcSaveSecurityQuestion, String username, String password);

    void triggerClearCategoryData();
}
