package com.tokopedia.profilecompletion.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel;

/**
 * Created by stevenfredian on 6/22/17.
 */

public interface ProfileCompletionContract extends CustomerView {

    interface View extends CustomerView{

        void skipView(String tag);

        void onGetUserInfo(ProfileCompletionViewModel profileCompletionViewModel);

        void onErrorGetUserInfo(String string);

        void onSuccessEditProfile(int edit);

        void onFailedEditProfile(String errorMessage);

        String getString(int id);

        void disableView();

        ProfileCompletionViewModel getData();

        void canProceed(boolean canProceed);

        Presenter getPresenter();

        android.view.View getView();
    }

    interface Presenter extends CustomerPresenter<View>{
        void getUserInfo();

        void editUserInfo(String date, int month, String year);

        void editUserInfo(int gender);

        void editUserInfo(String verif);

        void skipView(String tag);
    }
}
