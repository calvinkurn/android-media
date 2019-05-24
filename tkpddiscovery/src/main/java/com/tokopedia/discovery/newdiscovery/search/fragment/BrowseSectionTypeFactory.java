package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;

/**
 * Created by henrypriyono on 10/16/17.
 */

public interface BrowseSectionTypeFactory {
    int type(EmptySearchModel emptySearchModel);

    int getRecyclerViewItem();

    void setRecyclerViewItem(int recyclerViewItem);
}
