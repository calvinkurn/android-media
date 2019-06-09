package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.RelatedSearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.TopAdsViewModel;

/**
 * Created by henrypriyono on 10/11/17.
 */

public interface ProductListTypeFactory extends BrowseSectionTypeFactory {
    int type(ProductItem productItem);

    int type(HeaderViewModel headerViewModel);

    int type(TopAdsViewModel topAdsViewModel);

    int type(RelatedSearchModel relatedSearchModel);

    AbstractViewHolder createViewHolder(View view, int type);
}
