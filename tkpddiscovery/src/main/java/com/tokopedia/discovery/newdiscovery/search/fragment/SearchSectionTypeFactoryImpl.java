package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;

/**
 * Created by henrypriyono on 10/16/17.
 */

public abstract class SearchSectionTypeFactoryImpl extends BaseAdapterTypeFactory
        implements SearchSectionTypeFactory {

    private int recyclerViewItem;

    @Override
    public int getRecyclerViewItem() {
        return recyclerViewItem;
    }

    @Override
    public void setRecyclerViewItem(int recyclerViewItem) {
        this.recyclerViewItem = recyclerViewItem;
    }
}
