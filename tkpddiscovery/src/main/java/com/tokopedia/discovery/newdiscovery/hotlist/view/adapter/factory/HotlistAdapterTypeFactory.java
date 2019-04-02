package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.base.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.HotlistListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.BigGridProductViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.GridProductViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.HotlistHeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.ListProductViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.SearchEmptyViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.SearchEmptyViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistAdapterTypeFactory extends SearchSectionTypeFactoryImpl implements HotlistTypeFactory {

    private final HotlistListener mHotlistListener;
    private final String searchQuery;
    private final String hotlistAlias;

    public HotlistAdapterTypeFactory(HotlistListener mHotlistListener, String searchQuery, String hotlistAlias) {
        this.mHotlistListener = mHotlistListener;
        this.searchQuery = searchQuery;
        this.hotlistAlias = hotlistAlias;
    }

    @Override
    public int type(HotlistHeaderViewModel header) {
        return HotlistHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptySearchModel emptySearchModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public int type(HotlistProductViewModel product) {
        switch (getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return ListProductViewHolder.LAYOUT;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
                return BigGridProductViewHolder.LAYOUT;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
            default:
                return GridProductViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(SearchEmptyViewModel empty) {
        return SearchEmptyViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == HotlistHeaderViewHolder.LAYOUT) {
            viewHolder = new HotlistHeaderViewHolder(parent, mHotlistListener, searchQuery, hotlistAlias);
        } else if (type == BigGridProductViewHolder.LAYOUT) {
            viewHolder = new BigGridProductViewHolder(parent, mHotlistListener);
        } else if (type == GridProductViewHolder.LAYOUT) {
            viewHolder = new GridProductViewHolder(parent, mHotlistListener);
        } else if (type == ListProductViewHolder.LAYOUT) {
            viewHolder = new ListProductViewHolder(parent, mHotlistListener);
        } else if (type == SearchEmptyViewHolder.LAYOUT) {
            viewHolder  = new SearchEmptyViewHolder(parent, mHotlistListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }

}
