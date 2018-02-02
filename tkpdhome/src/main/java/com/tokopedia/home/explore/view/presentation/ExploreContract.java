package com.tokopedia.home.explore.view.presentation;


import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.home.beranda.data.source.pojo.HomeData;
import com.tokopedia.home.explore.domain.model.ExploreDataModel;

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

        void renderData(ExploreDataModel dataModel);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getData();

    }

}