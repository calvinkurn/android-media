package com.tokopedia.seller.product.picker.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public interface ProductListPickerSearchView extends BaseListViewListener<ProductListPickerViewModel> {

    void onSearchLoaded(@NonNull List<ProductListPickerViewModel> list, int totalItem, boolean hasNext);

}
