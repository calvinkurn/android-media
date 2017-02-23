package com.tokopedia.discovery.search.view.adapter.factory;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.search.view.SearchContract;
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
    public AbstractViewHolder createViewHolder(SearchContract.View parent, int type) {
        AbstractViewHolder viewHolder;
        if(type == DefaultSearchViewHolder.LAYOUT){
            viewHolder = new DefaultSearchViewHolder(parent, clickListener);
        } else if(type == ShopSearchViewHolder.LAYOUT){
            viewHolder = new ShopSearchViewHolder(parent, clickListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }

}