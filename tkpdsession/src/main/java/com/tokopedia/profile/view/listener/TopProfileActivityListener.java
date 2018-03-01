package com.tokopedia.profile.view.listener;


import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;

/**
 * @author by milhamj on 28/02/18.
 */

public interface TopProfileActivityListener {
    interface View extends CustomerView {
        void populateData(TopProfileViewModel viewModel);

        void showLoading();

        void hideLoading();

        void showErrorScreen(String errorMessage,
                             android.view.View.OnClickListener onClickListener);

        void hideErrorScreen();
    }
}
