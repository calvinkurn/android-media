package com.tokopedia.discovery.newdiscovery.category.presentation;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductViewModel;

import java.util.HashMap;

/**
 * @author by alifa on 10/25/17.
 */

public interface CategoryContract {

    interface View extends CustomerView {

        void prepareFragment(ProductViewModel productViewModel);

        void showLoading();

        void hideLoading();


    }

    interface Presenter extends CustomerPresenter<CategoryContract.View> {

        void getCategoryHeader(String categoryId, HashMap<String,String> filterParam);

        void getCategoryPage1(CategoryHeaderModel categoryHeaderModel);

    }
}
