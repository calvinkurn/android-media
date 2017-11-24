package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.GridCatalogViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.ListCatalogViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptyViewHolder;

/**
 * Created by hangnadi on 10/12/17.
 */

public class CatalogAdapterTypeFactory extends SearchSectionTypeFactoryImpl
        implements CatalogTypeFactory {

    private final ItemClickListener mItemClickListener;

    public CatalogAdapterTypeFactory(ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public int type(CatalogViewModel viewModel) {
        switch (getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return ListCatalogViewHolder.LAYOUT;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
            default:
                return GridCatalogViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == ListCatalogViewHolder.LAYOUT) {
            viewHolder = new ListCatalogViewHolder(parent, mItemClickListener);
        } else if (type == GridCatalogViewHolder.LAYOUT) {
            viewHolder = new GridCatalogViewHolder(parent, mItemClickListener);
        } else if (type == EmptyViewHolder.LAYOUT) {
            viewHolder = new EmptyViewHolder(parent, mItemClickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
