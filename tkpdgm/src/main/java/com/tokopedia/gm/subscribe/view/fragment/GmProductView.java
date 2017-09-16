package com.tokopedia.gm.subscribe.view.fragment;


import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.gm.subscribe.view.viewmodel.GmProductViewModel;

import java.util.List;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public interface GmProductView extends CustomerView {

    void renderProductList(List<GmProductViewModel> gmProductDomainModels);

    void errorGetProductList();

    void showProgressDialog();

    void dismissProgressDialog();

    void clearPackage();

    void setVisibilitySelectButton(boolean isView);
}
