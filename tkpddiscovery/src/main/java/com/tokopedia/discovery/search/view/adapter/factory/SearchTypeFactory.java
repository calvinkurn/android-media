package com.tokopedia.discovery.search.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.search.view.adapter.viewmodel.DefaultViewModel;
import com.tokopedia.discovery.search.view.adapter.viewmodel.ShopViewModel;

/**
 * @author erry on 14/02/17.
 */

public interface SearchTypeFactory {

    int type(DefaultViewModel viewModel);

    int type(ShopViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}