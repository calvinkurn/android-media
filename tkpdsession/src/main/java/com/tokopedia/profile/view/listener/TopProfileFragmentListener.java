package com.tokopedia.profile.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;

import java.util.List;

/**
 * @author by alvinatin on 28/02/18.
 */

public interface TopProfileFragmentListener {

    interface View extends CustomerView{
        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetProfileData(TopProfileViewModel topProfileViewModel);

        void onErrorGetProfileData(String message);
    }

    interface Presenter extends CustomerPresenter<View>{
        void initView(String userId);

        void getProfileContent(String userId);
    }
}
