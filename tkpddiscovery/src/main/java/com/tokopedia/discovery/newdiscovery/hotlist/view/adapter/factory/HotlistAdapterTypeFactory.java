package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.GridProductViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.HotlistHeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.ListProductViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder.SearchEmptyViewHolder;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.SearchEmptyViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;

/**
 * Created by hangnadi on 10/8/17.
 */

public class HotlistAdapterTypeFactory extends SearchSectionTypeFactoryImpl implements HotlistTypeFactory {

    private final ItemClickListener mItemClickListener;
    private final String searchQuery;

    public HotlistAdapterTypeFactory(ItemClickListener mItemClickListener, String searchQuery) {
        this.mItemClickListener = mItemClickListener;
        this.searchQuery = searchQuery;
    }

    @Override
    public int type(HotlistHeaderViewModel header) {
        return HotlistHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(HotlistProductViewModel product) {
        switch (getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return ListProductViewHolder.LAYOUT;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
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
            viewHolder = new HotlistHeaderViewHolder(parent, mItemClickListener, searchQuery);
        } else if (type == GridProductViewHolder.LAYOUT) {
            viewHolder = new GridProductViewHolder(parent, mItemClickListener);
        } else if (type == ListProductViewHolder.LAYOUT) {
            viewHolder = new ListProductViewHolder(parent, mItemClickListener);
        } else if (type == SearchEmptyViewHolder.LAYOUT) {
            viewHolder  = new SearchEmptyViewHolder(parent, mItemClickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }

}
