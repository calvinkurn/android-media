package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.CatalogHeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.GridCatalogViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.ListCatalogViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptyViewHolder;
import com.tokopedia.topads.sdk.base.Config;

/**
 * Created by hangnadi on 10/12/17.
 */

public class CatalogAdapterTypeFactory extends SearchSectionTypeFactoryImpl
        implements CatalogTypeFactory {

    private final ItemClickListener mItemClickListener;
    private final Config topAdsConfig;

    public CatalogAdapterTypeFactory(ItemClickListener listener, Config topAdsConfig) {
        this.mItemClickListener = listener;
        this.topAdsConfig = topAdsConfig;
    }

    @Override
    public int type(CatalogHeaderViewModel headerViewModel) {
        return CatalogHeaderViewHolder.LAYOUT;
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
        } else if (type == CatalogHeaderViewHolder.LAYOUT) {
            viewHolder = new CatalogHeaderViewHolder(parent, mItemClickListener, topAdsConfig);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
