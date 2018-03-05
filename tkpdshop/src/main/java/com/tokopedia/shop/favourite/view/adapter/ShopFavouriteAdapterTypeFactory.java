package com.tokopedia.shop.favourite.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.favourite.view.adapter.viewholder.ShopFavouriteViewHolder;
import com.tokopedia.shop.favourite.view.model.ShopFavouriteViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopFavouriteAdapterTypeFactory extends BaseAdapterTypeFactory {

    public int type(ShopFavouriteViewModel shopFavouriteViewModel) {
        return ShopFavouriteViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopFavouriteViewHolder.LAYOUT) {
            viewHolder = new ShopFavouriteViewHolder(view);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }
}
