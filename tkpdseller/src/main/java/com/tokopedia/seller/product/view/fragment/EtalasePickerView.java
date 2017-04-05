package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.view.model.etalase.MyEtalaseViewModel;

import java.util.List;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface EtalasePickerView extends CustomerView {
    void showLoading();

    void dismissLoading();

    void renderEtalaseList(List<MyEtalaseViewModel> etalases);
}
