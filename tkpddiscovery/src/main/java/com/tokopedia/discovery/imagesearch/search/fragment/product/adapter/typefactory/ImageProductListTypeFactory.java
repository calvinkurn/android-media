package com.tokopedia.discovery.imagesearch.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageProductListTypeFactory extends SearchSectionTypeFactory {

    int type(ProductItem productItem);

    int type(HeaderViewModel headerViewModel);

    int type(GuidedSearchViewModel guidedSearchViewModel);

    AbstractViewHolder createViewHolder(View view, int type);
}
