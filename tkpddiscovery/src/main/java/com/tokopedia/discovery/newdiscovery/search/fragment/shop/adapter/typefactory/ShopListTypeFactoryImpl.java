package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptySearchViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.listener.ShopListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.viewholder.GridShopItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.viewholder.ListShopItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class ShopListTypeFactoryImpl extends SearchSectionTypeFactoryImpl implements ShopListTypeFactory {

    private ShopListener itemClickListener;

    public ShopListTypeFactoryImpl(ShopListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int type(ShopViewModel.ShopItem shopItem) {
        switch (getRecyclerViewItem()) {
            case TkpdState.RecyclerView.VIEW_PRODUCT:
                return ListShopItemViewHolder.LAYOUT;
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_1:
            case TkpdState.RecyclerView.VIEW_PRODUCT_GRID_2:
            default:
                return GridShopItemViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptySearchModel emptySearchModel) {
        return EmptySearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ListShopItemViewHolder.LAYOUT) {
            viewHolder = new ListShopItemViewHolder(view, itemClickListener);
        } else if (type == GridShopItemViewHolder.LAYOUT) {
            viewHolder = new GridShopItemViewHolder(view, itemClickListener);
        } else if (type == EmptySearchViewHolder.LAYOUT) {
            viewHolder = new EmptySearchViewHolder(view, itemClickListener, null);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
