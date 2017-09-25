package com.tokopedia.gm.subscribe.view.fragment;


import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by sebastianuskh on 2/9/17.
 */

public interface GmHomeView extends CustomerView {
    void showProgressDialog();

    void dismissProgressDialog();
}
