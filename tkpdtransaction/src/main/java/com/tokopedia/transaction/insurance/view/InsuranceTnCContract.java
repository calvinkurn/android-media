package com.tokopedia.transaction.insurance.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public interface InsuranceTnCContract {

    interface View extends CustomerView {
        void showWebView(String webViewData);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadWebViewData();
    }
}
