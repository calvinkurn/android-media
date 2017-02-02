package com.tokopedia.seller.gmsubscribe.view.product.presenter;


import com.tokopedia.seller.gmsubscribe.view.product.viewmodel.GMProductViewModel;

import java.util.List;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public interface GMProductView {

    void renderProductList(List<GMProductViewModel> gmProductDomainModels);
}
