package com.tokopedia.seller.product.view.fragment;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.data.model.data.Etalase;

import java.util.List;

/**
 * @author sebastianuskh on 4/5/17.
 */

public interface EtalasePickerView extends CustomerView {
    void showLoading();

    void dismissLoading();

    void renderEtalaseList(List<Etalase> etalases);
}
