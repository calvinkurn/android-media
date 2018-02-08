package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptySearchViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.GridProductItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.ListProductItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.HeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptyViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.topads.sdk.base.Config;

/**
 * Created by henrypriyono on 10/11/17.
 */

public class ProductListTypeFactoryImpl extends SearchSectionTypeFactoryImpl implements ProductListTypeFactory {

    private final ItemClickListener itemClickListener;
    private final Config topAdsConfig;

    public ProductListTypeFactoryImpl(ItemClickListener itemClickListener, Config config) {
        this.itemClickListener = itemClickListener;
        this.topAdsConfig = config;
    }

    @Override
    public int type(HeaderViewModel headerViewModel) {
        return HeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductItem productItem) {
        switch (getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return ListProductItemViewHolder.LAYOUT;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
            default:
                return GridProductItemViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptySearchModel emptySearchModel) {
        return EmptySearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ListProductItemViewHolder.LAYOUT) {
            viewHolder = new ListProductItemViewHolder(view, itemClickListener);
        } else if (type == GridProductItemViewHolder.LAYOUT) {
            viewHolder = new GridProductItemViewHolder(view, itemClickListener);
        } else if(type == HeaderViewHolder.LAYOUT){
            viewHolder = new HeaderViewHolder(view, itemClickListener, topAdsConfig);
        } else if (type == EmptySearchViewHolder.LAYOUT) {
            viewHolder = new EmptySearchViewHolder(view, itemClickListener, topAdsConfig);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
