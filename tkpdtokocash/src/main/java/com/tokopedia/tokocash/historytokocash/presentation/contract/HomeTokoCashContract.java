package com.tokopedia.tokocash.historytokocash.presentation.contract;

import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

/**
 * Created by nabillasabbaha on 2/6/18.
 */

public interface HomeTokoCashContract {

    interface View extends CustomerView {
        void showProgressLoading();

        void hideProgressLoading();

        void showToastMessage(String message);

        void showErrorMessage();

        void renderBalanceTokoCash(BalanceTokoCash balanceTokoCash);

        void showEmptyPage(Throwable throwable);

        void navigateToActivityRequest(Intent intent, int requestCode);
    }

    interface Presenter extends CustomerPresenter<View> {
        void processGetCategoryTopUp();

        void processGetBalanceTokoCash();
    }
}
