package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.DefaultCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.RevampCategoryAdapter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder.CategoryDefaultHeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder.CategoryLifestyleHeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder.CategoryRevampHeaderViewHolder;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder.GridProductItemViewHolder;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder.ListProductItemViewHolder;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptyViewHolder;
import com.tokopedia.topads.sdk.base.Config;

/**
 * Created by henrypriyono on 10/11/17.
 * Edited by alifa handle category
 */

public class CategoryProductListTypeFactoryImpl extends SearchSectionTypeFactoryImpl implements CategoryProductListTypeFactory {

    private static final String TEMPLATE_LIFESTYLE = "LIFESTYLE";
    private final ItemClickListener itemClickListener;
    private final DefaultCategoryAdapter.CategoryListener categoryListener;
    private final RevampCategoryAdapter.CategoryListener categoryRevampListener;
    private final Config topAdsConfig;

    public CategoryProductListTypeFactoryImpl(ItemClickListener itemClickListener,
                                              DefaultCategoryAdapter.CategoryListener categoryListener,
                                              RevampCategoryAdapter.CategoryListener categoryRevampListener,
                                              Config topAdsConfig) {
        this.itemClickListener = itemClickListener;
        this.categoryListener = categoryListener;
        this.categoryRevampListener = categoryRevampListener;
        this.topAdsConfig = topAdsConfig;
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
    public int type(CategoryHeaderModel categoryHeaderModel) {
        if (categoryHeaderModel.isRevamp()) {
            if (categoryHeaderModel.getTemplate().equals(TEMPLATE_LIFESTYLE)) {
                return CategoryLifestyleHeaderViewHolder.LAYOUT;
            } else {
                return CategoryRevampHeaderViewHolder.LAYOUT;
            }
        }
        return CategoryDefaultHeaderViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == CategoryDefaultHeaderViewHolder.LAYOUT) {
            viewHolder = new CategoryDefaultHeaderViewHolder(view,categoryListener);
        } else if (type == CategoryRevampHeaderViewHolder.LAYOUT) {
            viewHolder = new CategoryRevampHeaderViewHolder(view,categoryRevampListener);
        } else if (type == ListProductItemViewHolder.LAYOUT) {
            viewHolder = new ListProductItemViewHolder(view, itemClickListener);
        } else if (type == GridProductItemViewHolder.LAYOUT) {
            viewHolder = new GridProductItemViewHolder(view, itemClickListener);
        } else if (type == CategoryLifestyleHeaderViewHolder.LAYOUT) {
            viewHolder = new CategoryLifestyleHeaderViewHolder(view, categoryRevampListener);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
