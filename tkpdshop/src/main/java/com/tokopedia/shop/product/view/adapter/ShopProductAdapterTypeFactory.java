package com.tokopedia.shop.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductSingleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductClickedListener shopProductClickedListener;

    public ShopProductAdapterTypeFactory(ShopProductClickedListener shopProductClickedListener) {
        this.shopProductClickedListener = shopProductClickedListener;
    }

    public int type(ShopProductViewModel shopProductViewModel) {
        return ShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopProductViewHolder.LAYOUT) {
            viewHolder = new ShopProductViewHolder(view, shopProductClickedListener);
        } else if (viewType == ShopProductListViewHolder.LAYOUT) {
            viewHolder = new ShopProductListViewHolder(view, shopProductClickedListener);
        } else if (viewType == ShopProductSingleViewHolder.LAYOUT) {
            viewHolder = new ShopProductSingleViewHolder(view, shopProductClickedListener);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }

}
