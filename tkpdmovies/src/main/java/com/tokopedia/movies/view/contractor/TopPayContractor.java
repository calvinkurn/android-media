package com.tokopedia.movies.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.movies.view.viewmodel.PaymentPassData;

/**
 * Created by pranaymohapatra on 15/12/17.
 */

public interface TopPayContractor {

    public interface ITopPayView extends CustomerView{
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);


        RequestParams getParams();

        void renderWebViewPostUrl(String url, byte[] postData);

        void showToastMessageWithForceCloseView(String message);

        void showToastMessage(String message);

        void callbackPaymentCanceled();

        void callbackPaymentFailed();

        void callbackPaymentSucceed();

        void hideProgressBar();

        void showProgressBar();

        void showTimeoutErrorOnUiThread();

        void setWebPageTitle(String title);

        void backStackAction();

        String getStringFromResource(int resId);

        PaymentPassData getPaymentPassData();

        void navigateToActivity(Intent intentCart);
    }
    public interface Presenter extends CustomerPresenter<ITopPayView>{
        public void proccessUriPayment();
    }
}
