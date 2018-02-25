package com.tokopedia.shop.product.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseSelectedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductEtalaseUnselectedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductSingleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory implements ShopProductTypeFactory {

    private TypeFactoryListener typeFactoryListener;
    private ShopProductViewHolder.ShopProductVHListener shopProductVHListener;

    public ShopProductAdapterTypeFactory() {}

    public ShopProductAdapterTypeFactory(
            @Nullable TypeFactoryListener typeFactoryListener,
            @Nullable ShopProductViewHolder.ShopProductVHListener shopProductVHListener

    ) {
        this.typeFactoryListener = typeFactoryListener;
        this.shopProductVHListener = shopProductVHListener;
    }

    @Override
    public int type(ShopProductViewModel shopProductViewModel) {
        if(typeFactoryListener != null){
            return typeFactoryListener.getType(shopProductViewModel);
        }
        return ShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopProductViewHolder.LAYOUT) {
            viewHolder = new ShopProductViewHolder(view)
                    .setShopProductVHListener(shopProductVHListener);
        } else if(viewType == ShopProductListViewHolder.LAYOUT){
            viewHolder = new ShopProductListViewHolder(view)
                    .setShopProductVHListener(shopProductVHListener);
        } else if(viewType == ShopProductSingleViewHolder.LAYOUT){
            viewHolder = new ShopProductSingleViewHolder(view)
                    .setShopProductVHListener(shopProductVHListener);
        } else if(viewType == ShopProductEtalaseUnselectedViewHolder.LAYOUT){
            viewHolder = new ShopProductEtalaseUnselectedViewHolder(view);
        } else if(viewType == ShopProductEtalaseSelectedViewHolder.LAYOUT){
            viewHolder = new ShopProductEtalaseSelectedViewHolder(view);
        } else{
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }

    public interface TypeFactoryListener<E>{
        int getType(E type);
    }
}
