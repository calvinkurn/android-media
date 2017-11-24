package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

/**
 * Created by henrypriyono on 10/13/17.
 */

public interface ShopListTypeFactory extends SearchSectionTypeFactory {
    int type(ShopViewModel.ShopItem shopItem);

    int type(EmptyModel emptyModel);

    AbstractViewHolder createViewHolder(View view, int type);
}
