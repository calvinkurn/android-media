package com.tokopedia.shop.product.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseSelectedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseUnselectedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductSingleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductLimitedAdapterTypeFactory extends ShopProductAdapterTypeFactory{
    public ShopProductLimitedAdapterTypeFactory() {
    }

    public ShopProductLimitedAdapterTypeFactory(@Nullable TypeFactoryListener typeFactoryListener, @Nullable ShopProductViewHolder.ShopProductVHListener shopProductVHListener) {
        super(typeFactoryListener, shopProductVHListener);
    }
}
