package com.tokopedia.session.session.presenter;

import com.tokopedia.core.presenter.BaseView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by m.normansyah on 1/22/16.
 */
@Deprecated
public interface RegisterNewView extends BaseView {
    String TAG = "MNORMANSYAH";
    String messageTAG = "RegisterNewView ";

    List<String> getEmailListOfAccountsUserHasLoggedInto();

    boolean isEmailAddressFromDevice();

    void setupEmailAddressToEmailTextView();

    void initView();

    void alertbox(String mymessage);

    void attemptRegisterStep1();

    void enableDisableAllFieldsForEmailValidationForm(boolean isEnable);

    void validateEmailAddressTask();

    void showProgress(final boolean show);

    void showErrorValidateEmail();

    void moveToRegisterNext(String name, String email, String password);

    void alertBox();

    void setData(HashMap<String, Object> data);

    void finishActivity();

    void showErrorValidateEmail(String s);

    void showError(String string);
}
