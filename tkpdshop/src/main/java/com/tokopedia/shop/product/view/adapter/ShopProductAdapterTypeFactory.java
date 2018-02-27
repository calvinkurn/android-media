package com.tokopedia.shop.product.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFilterSelectedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFilterUnselectedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductSingleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory {

    private ShopProductFilterAdapterTypeFactory.TypeFactoryListener typeFactoryListener;
    private ShopProductViewHolder.ShopProductVHListener shopProductVHListener;

    public ShopProductAdapterTypeFactory(
            @Nullable ShopProductFilterAdapterTypeFactory.TypeFactoryListener typeFactoryListener,
            @Nullable ShopProductViewHolder.ShopProductVHListener shopProductVHListener

    ) {
        this.typeFactoryListener = typeFactoryListener;
        this.shopProductVHListener = shopProductVHListener;
    }

    public int type(ShopProductViewModel shopProductViewModel) {
        if (typeFactoryListener != null) {
            return typeFactoryListener.getType(shopProductViewModel);
        }
        return ShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopProductViewHolder.LAYOUT) {
            viewHolder = new ShopProductViewHolder(view).setShopProductVHListener(shopProductVHListener);
        } else if (viewType == ShopProductListViewHolder.LAYOUT) {
            viewHolder = new ShopProductListViewHolder(view).setShopProductVHListener(shopProductVHListener);
        } else if (viewType == ShopProductSingleViewHolder.LAYOUT) {
            viewHolder = new ShopProductSingleViewHolder(view).setShopProductVHListener(shopProductVHListener);
        } else if (viewType == ShopProductFilterUnselectedViewHolder.LAYOUT) {
            viewHolder = new ShopProductFilterUnselectedViewHolder(view);
        } else if (viewType == ShopProductFilterSelectedViewHolder.LAYOUT) {
            viewHolder = new ShopProductFilterSelectedViewHolder(view);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }

}
