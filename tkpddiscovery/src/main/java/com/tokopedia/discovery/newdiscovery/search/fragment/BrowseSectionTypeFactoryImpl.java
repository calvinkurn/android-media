package com.tokopedia.discovery.newdiscovery.search.fragment;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;

/**
 * Created by henrypriyono on 10/16/17.
 */

public abstract class BrowseSectionTypeFactoryImpl extends BaseAdapterTypeFactory
        implements BrowseSectionTypeFactory {

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
