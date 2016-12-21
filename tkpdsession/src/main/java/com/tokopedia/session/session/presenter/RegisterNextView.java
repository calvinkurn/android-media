package com.tokopedia.session.session.presenter;

import com.tokopedia.core.presenter.BaseView;

import java.util.List;

/**
 * Created by m.normansyah on 13/11/2015.
 */
public interface RegisterNextView extends BaseView {
    String FULLNAME = "FULLNAME";
    String PHONENUMBER = "PHONENUMBER";
    String GENDER = "GENDER";
    String BIRTHDAYTTL = "BIRTHDAYTTL";
    String EMAIL = "EMAIL";

    void attemptRegister();
    void setTermPrivacyText(String text);
    void showProgress(final boolean show);
    void setEmailText(String text);
    void setPasswordText(String text);
    void setConfirmPasswordText(String text);
    void toggleAgreement(boolean agree);
    void showMessageError(List<String> MessageError);
}
