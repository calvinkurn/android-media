package com.tokopedia.discovery.newdiscovery.category.presentation;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;

import java.util.HashMap;

/**
 * @author by alifa on 10/25/17.
 */

public interface CategoryContract {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();


    }

    interface Presenter extends CustomerPresenter<CategoryContract.View> {

        void getCategoryHeader(String categoryId, HashMap<String,String> filterParam);

    }
}
