package com.tokopedia.seller.gmsubscribe.view.home.fragment;


import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by sebastianuskh on 2/9/17.
 */

public interface GmHomeView extends CustomerView {
    void showProgressDialog();

    void dismissProgressDialog();
}
