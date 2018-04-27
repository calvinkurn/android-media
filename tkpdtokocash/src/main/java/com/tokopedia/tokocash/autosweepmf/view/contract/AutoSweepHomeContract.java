package com.tokopedia.tokocash.autosweepmf.view.contract;

import android.content.Context;

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

        void onSuccessAutoSweepDetail(AutoSweepDetail data);

        void onErrorAutoSweepDetail(String error);

        void onAutoSweepActive();

        void onAutoSweepInActive();

        void showDialog();

        void initDialog(AutoSweepDetail data);

        Context getAppContext();

        Context getActivityContext();

        void openWebView(String url);

        void onSuccessAutoSweepStatus(AutoSweepLimit data);

        void onErrorAutoSweepStatus(String error);

        void retry();
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void getAutoSweepDetail();

        void updateAutoSweepStatus(boolean isEnable, int amount);
    }
}
