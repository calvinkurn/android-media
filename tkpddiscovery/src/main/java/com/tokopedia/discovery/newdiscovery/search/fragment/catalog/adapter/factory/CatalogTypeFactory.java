package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;

/**
 * Created by hangnadi on 10/12/17.
 */

public interface CatalogTypeFactory extends SearchSectionTypeFactory {

    int type(CatalogViewModel viewModel);

    int type(EmptyModel emptyModel);
    int type(CatalogHeaderViewModel headerViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
