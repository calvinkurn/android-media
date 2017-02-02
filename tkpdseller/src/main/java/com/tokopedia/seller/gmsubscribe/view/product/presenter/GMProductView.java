package com.tokopedia.seller.gmsubscribe.view.product.presenter;


import com.tokopedia.seller.common.presentation.CustomerView;
import com.tokopedia.seller.gmsubscribe.view.product.viewmodel.GMProductViewModel;

import java.util.List;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public interface GMProductView extends CustomerView {

    void renderProductList(List<GMProductViewModel> gmProductDomainModels);
}
