package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.search.fragment.BrowseSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.BigGridCatalogViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.CatalogHeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.GridCatalogViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder.ListCatalogViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogHeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptySearchViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.topads.sdk.base.Config;

/**
 * Created by hangnadi on 10/12/17.
 */

public class CatalogAdapterTypeFactory extends BrowseSectionTypeFactoryImpl
        implements CatalogTypeFactory {

    private final CatalogListener mCatalogListener;
    private final Config topAdsConfig;

    public CatalogAdapterTypeFactory(CatalogListener listener, Config topAdsConfig) {
        this.mCatalogListener = listener;
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
                return BigGridCatalogViewHolder.LAYOUT;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
            default:
                return GridCatalogViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptySearchModel emptySearchModel) {
        return EmptySearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == ListCatalogViewHolder.LAYOUT) {
            viewHolder = new ListCatalogViewHolder(parent, mCatalogListener);
        } else if (type == GridCatalogViewHolder.LAYOUT) {
            viewHolder = new GridCatalogViewHolder(parent, mCatalogListener);
        } else if (type == BigGridCatalogViewHolder.LAYOUT) {
            viewHolder = new BigGridCatalogViewHolder(parent, mCatalogListener);
        } else if (type == EmptySearchViewHolder.LAYOUT) {
            viewHolder = new EmptySearchViewHolder(parent, mCatalogListener, null);
        } else if (type == CatalogHeaderViewHolder.LAYOUT) {
            viewHolder = new CatalogHeaderViewHolder(parent, mCatalogListener, topAdsConfig);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
