package com.tokopedia.seller.product.manage.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.manage.view.model.SortManageProductModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/29/17.
 */

public interface SortManageProductView extends CustomerView {
    void onSuccessGetListSort(List<SortManageProductModel> sortManageProductModels);
}
