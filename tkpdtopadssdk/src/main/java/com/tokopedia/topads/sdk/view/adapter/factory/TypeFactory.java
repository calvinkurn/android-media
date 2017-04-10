package com.tokopedia.topads.sdk.view.adapter.factory;

import android.view.View;

import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ProductListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.ShopListViewModel;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public interface TypeFactory {

    int type(ProductGridViewModel viewModel);

    int type(ProductListViewModel viewModel);

    int type(ShopGridViewModel viewModel);

    int type(ShopListViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
