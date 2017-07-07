package com.tokopedia.profilecompletion.view.presenter;

import android.view.View;

/**
 * Created by stevenfredian on 6/22/17.
 */

public interface ProfileCompletionPresenter {
    void getUserInfo();

    void editUserInfo(String date, int month, String year);

    void editUserInfo(int gender);

    void skipView(String tag);

    void editUserInfo(String verif);
}
