package com.tokopedia.profile.view.listener;


import android.content.Context;

import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;

/**
 * @author by milhamj on 28/02/18.
 */

public interface TopProfileActivityListener {
    interface View extends CustomerView {
        Context getContext();

        SessionRouter getSessionRouter();

        void showMainView();

        void hideMainView();

        void showLoading();

        void hideLoading();

        void showErrorScreen(String errorMessage,
                             android.view.View.OnClickListener onClickListener);

        void hideErrorScreen();

        void onSuccessFollowKol();

        void onErrorFollowKol(String message);

        void onSuccessGetProfileData(TopProfileViewModel topProfileViewModel);

        void onErrorGetProfileData(String message);
    }

    interface Presenter extends CustomerPresenter<View>{
        void initView(String userId);

        void getTopProfileData(String userId);

        void followKol(String userId);

        void unfollowKol(String userId);
    }
}
