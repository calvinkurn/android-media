package com.tokopedia.discovery.imagesearch.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptyViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.GridProductItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.GuidedSearchViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.HeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.ImageEmptySearchViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.ListProductItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.RelatedSearchViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.TopAdsViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.HeaderViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.RelatedSearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.TopAdsViewModel;
import com.tokopedia.topads.sdk.base.Config;

/**
 * Created by sachinbansal on 4/13/18.
 */

public class ImageProductListTypeFactoryImpl extends SearchSectionTypeFactoryImpl implements ProductListTypeFactory {

    private final ProductListener itemClickListener;
    private final Config topAdsConfig;
    private final String searchQuery;

    public ImageProductListTypeFactoryImpl(ProductListener itemClickListener, Config config, String searchQuery) {
        this.itemClickListener = itemClickListener;
        this.topAdsConfig = config;
        this.searchQuery = searchQuery;
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
    public int type(GuidedSearchViewModel guidedSearchViewModel) {
        return GuidedSearchViewHolder.LAYOUT;
    }

    @Override
    public int type(TopAdsViewModel topAdsViewModel) {
        return TopAdsViewHolder.LAYOUT;
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
        return ImageEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(RelatedSearchModel relatedSearchModel) {
        return RelatedSearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == ListProductItemViewHolder.LAYOUT) {
            viewHolder = new ListProductItemViewHolder(view, itemClickListener);
        } else if (type == GridProductItemViewHolder.LAYOUT) {
            viewHolder = new GridProductItemViewHolder(view, itemClickListener);
        } else if(type == HeaderViewHolder.LAYOUT){
            viewHolder = new HeaderViewHolder(view, itemClickListener, searchQuery);
        } else if (type == ImageEmptySearchViewHolder.LAYOUT) {
            viewHolder = new ImageEmptySearchViewHolder(view, itemClickListener, topAdsConfig);
        } else if (type == GuidedSearchViewHolder.LAYOUT) {
            viewHolder = new GuidedSearchViewHolder(view, itemClickListener);
        } else if (type == TopAdsViewHolder.LAYOUT) {
            viewHolder = new TopAdsViewHolder(view, itemClickListener);
        } else if (type == RelatedSearchViewHolder.LAYOUT) {
            viewHolder = new RelatedSearchViewHolder(view, itemClickListener);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }

}
