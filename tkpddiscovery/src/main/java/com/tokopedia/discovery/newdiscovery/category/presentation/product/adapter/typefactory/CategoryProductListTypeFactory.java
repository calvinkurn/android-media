package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;

/**
 * Created by henrypriyono on 10/11/17.
 */

public interface CategoryProductListTypeFactory extends SearchSectionTypeFactory {
    int type(ProductItem productItem);

    int type(CategoryHeaderModel categoryHeaderModel);

    AbstractViewHolder createViewHolder(View view, int type);
}
