package com.tokopedia.shop.product.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductTitleHeaderModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductLimitedAdapterTypeFactory extends ShopProductAdapterTypeFactory{
    public ShopProductLimitedAdapterTypeFactory() {
    }

    public ShopProductLimitedAdapterTypeFactory(@Nullable TypeFactoryListener typeFactoryListener,
                                                @Nullable ShopProductViewHolder.ShopProductVHListener shopProductVHListener) {
        super(typeFactoryListener, shopProductVHListener);
    }

    public int type(ShopProductTitleHeaderModel shopProductTitleHeaderModel) {
        return ShopProductTitleViewHolder.LAYOUT;
    }

    public int type(ShopProductFeaturedViewModel shopProductFeaturedViewModel) {
        return ShopProductFeaturedViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ShopProductTitleViewHolder.LAYOUT) {
            return new ShopProductTitleViewHolder(parent);
        } else if (type == ShopProductFeaturedViewHolder.LAYOUT) {
            return new ShopProductFeaturedViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}