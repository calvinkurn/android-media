package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;

/**
 * Created by henrypriyono on 10/11/17.
 */

public interface ProductListTypeFactory extends SearchSectionTypeFactory {
    int type(ProductItem productItem);

    int type(EmptySearchModel emptySearchModel);

    int type(HeaderViewModel headerViewModel);

    int type(GuidedSearchViewModel guidedSearchViewModel);

    AbstractViewHolder createViewHolder(View view, int type);
}
