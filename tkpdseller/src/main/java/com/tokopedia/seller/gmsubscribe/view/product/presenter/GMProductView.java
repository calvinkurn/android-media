package com.tokopedia.seller.gmsubscribe.view.product.presenter;


import com.tokopedia.seller.common.presentation.CustomerView;
import com.tokopedia.seller.gmsubscribe.view.product.viewmodel.GmProductViewModel;

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
}
