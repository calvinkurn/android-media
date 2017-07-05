package com.tokopedia.profilecompletion.view.presenter;

/**
 * @author by nisie on 6/19/17.
 */

public interface ProfilePhoneVerifCompletionPresenter {
    void getUserInfo();

    void editDOB(String day, String month, String year);

    void editGender(int i);
}
