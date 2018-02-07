package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptyViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.ListProductItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.viewholder.GridShopItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.viewholder.ListShopItemViewHolder;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class ShopListTypeFactoryImpl extends SearchSectionTypeFactoryImpl implements ShopListTypeFactory {

    private ItemClickListener itemClickListener;

    public ShopListTypeFactoryImpl(ItemClickListener itemClickListener) {
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
    public int type(EmptyModel retryModel) {
        return EmptyViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ListShopItemViewHolder.LAYOUT) {
            viewHolder = new ListShopItemViewHolder(view, itemClickListener);
        } else if (type == GridShopItemViewHolder.LAYOUT) {
            viewHolder = new GridShopItemViewHolder(view, itemClickListener);
        } else if (type == EmptyViewHolder.LAYOUT) {
            viewHolder = new EmptyViewHolder(view, itemClickListener);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}
