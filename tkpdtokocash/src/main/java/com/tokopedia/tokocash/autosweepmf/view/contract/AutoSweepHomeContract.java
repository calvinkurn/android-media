package com.tokopedia.tokocash.autosweepmf.view.contract;

import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;

public interface AutoSweepHomeContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showError(String error);

        void learMore();

        void navigateToLimitPage();

        void showToast(String message);

        void onSuccessAutoSweepDetail(AutoSweepDetail data);

        void onErrorAutoSweepDetail(String error);

        void onAccountHold(AutoSweepDetail data);

        void onAccountInActive();

        void onAccountActive(AutoSweepDetail data);

        void onAutoSweepActive();

        void onAutoSweepInActive();

        void showDialog(@StringRes int title, @StringRes int content);

        Context getAppContext();

        Context getActivityContext();

        void openWebView(String url);

        void onSuccessAutoSweepStatus(AutoSweepLimit data);

        void onErrorAutoSweepStatus(String error);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getAutoSweepDetail();

        void updateAutoSweepStatus(boolean isEnable, int amount);
    }
}
