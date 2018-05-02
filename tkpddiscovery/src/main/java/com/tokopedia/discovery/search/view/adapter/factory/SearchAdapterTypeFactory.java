package com.tokopedia.discovery.search.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.autocomplete.adapter.AutoCompleteViewHolder;
import com.tokopedia.discovery.autocomplete.adapter.CategoryViewHolder;
import com.tokopedia.discovery.autocomplete.adapter.DigitalViewHolder;
import com.tokopedia.discovery.autocomplete.adapter.InCategoryViewHolder;
import com.tokopedia.discovery.autocomplete.adapter.ShopViewHolder;
import com.tokopedia.discovery.autocomplete.adapter.TitleViewHolder;
import com.tokopedia.discovery.autocomplete.viewmodel.AutoCompleteSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.CategorySearch;
import com.tokopedia.discovery.autocomplete.viewmodel.DigitalSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.InCategorySearch;
import com.tokopedia.discovery.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.ShopSearch;
import com.tokopedia.discovery.autocomplete.viewmodel.TitleSearch;
import com.tokopedia.discovery.search.view.adapter.ItemClickListener;
import com.tokopedia.discovery.search.view.adapter.viewholder.DefaultSearchViewHolder;
import com.tokopedia.discovery.search.view.adapter.viewholder.ShopSearchViewHolder;
import com.tokopedia.discovery.search.view.adapter.viewmodel.DefaultViewModel;
import com.tokopedia.discovery.search.view.adapter.viewmodel.ShopViewModel;

/**
 * @author erry on 20/02/17.
 */

public class SearchAdapterTypeFactory extends BaseAdapterTypeFactory implements SearchTypeFactory {

    private final ItemClickListener clickListener;

    public SearchAdapterTypeFactory(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int type(DefaultViewModel viewModel) {
        return DefaultSearchViewHolder.LAYOUT;
    }

    @Override
    public int type(ShopViewModel viewModel) {
        return ShopSearchViewHolder.LAYOUT;
    }

    @Override
    public int type(TitleSearch viewModel) {
        return TitleViewHolder.LAYOUT;
    }

    @Override
    public int type(DigitalSearch viewModel) {
        return DigitalViewHolder.LAYOUT;
    }

    @Override
    public int type(CategorySearch viewModel) {
        return CategoryViewHolder.LAYOUT;
    }

    @Override
    public int type(InCategorySearch viewModel) {
        return InCategoryViewHolder.LAYOUT;
    }

    @Override
    public int type(PopularSearch viewModel) {
        return 0;
    }

    @Override
    public int type(RecentSearch viewModel) {
        return 0;
    }

    @Override
    public int type(ShopSearch viewModel) {
        return ShopViewHolder.LAYOUT;
    }

    @Override
    public int type(AutoCompleteSearch viewModel) {
        return AutoCompleteViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if(type == DefaultSearchViewHolder.LAYOUT){
            viewHolder = new DefaultSearchViewHolder(parent, clickListener);
        } else if(type == ShopSearchViewHolder.LAYOUT){
            viewHolder = new ShopSearchViewHolder(parent, clickListener);
        } else if(type == DigitalViewHolder.LAYOUT) {
            viewHolder = new DigitalViewHolder(parent, clickListener);
        } else if(type == TitleViewHolder.LAYOUT) {
            viewHolder = new TitleViewHolder(parent);
        } else if(type == CategoryViewHolder.LAYOUT) {
            viewHolder = new CategoryViewHolder(parent, clickListener);
        } else if(type == InCategoryViewHolder.LAYOUT) {
            viewHolder = new InCategoryViewHolder(parent, clickListener);
        } else if(type == ShopViewHolder.LAYOUT) {
            viewHolder = new ShopViewHolder(parent, clickListener);
        } else if(type == AutoCompleteViewHolder.LAYOUT) {
            viewHolder = new AutoCompleteViewHolder(parent, clickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }

}