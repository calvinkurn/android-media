package com.tokopedia.home.explore.view.presentation;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.explore.domain.model.CategoryLayoutRowModel;

import java.util.List;

/**
 * Created by errysuprayogi on 1/30/18.
 */

public interface ExploreContract {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        void showNetworkError(String message);

        void removeNetworkError();

        String getString(@StringRes int res);

        Context getContext();

        void openWebViewURL(String url, Context context);

        Activity getActivity();

    }

    interface Presenter extends CustomerPresenter<View> {

    }

}