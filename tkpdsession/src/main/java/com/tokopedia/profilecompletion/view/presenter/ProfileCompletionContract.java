package com.tokopedia.profilecompletion.view.presenter;

import android.view.View;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;

/**
 * Created by stevenfredian on 6/22/17.
 */

public interface ProfileCompletionContract extends CustomerView {

    interface View extends CustomerView{

        void skipView(String tag);

        void onGetUserInfo(GetUserInfoDomainData getUserInfoDomainData);

        void onErrorGetUserInfo(String string);

        void onSuccessEditProfile(int edit);

        void onFailedEditProfile(String errorMessage);

        String getString(int id);

        void disableView();
    }

    interface Presenter extends CustomerPresenter<View>{
        void getUserInfo();

        void editUserInfo(String date, int month, String year);

        void editUserInfo(int gender);

        void editUserInfo(String verif);

        void skipView(String tag);
    }
}
