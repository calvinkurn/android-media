package com.tokopedia.shop.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.address.view.model.ShopAddressViewModel;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductListViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductListViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductAdapterTypeFactory extends BaseAdapterTypeFactory implements ShopProductTypeFactory {

    private ShopProductViewHolder.ViewHolderListener viewHolderListener;
    private TypeFactoryListener typeFactoryListener;

    public ShopProductAdapterTypeFactory(
            ShopProductViewHolder.ViewHolderListener viewHolderListener,
            TypeFactoryListener typeFactoryListener
    ) {
        this.viewHolderListener = viewHolderListener;
        this.typeFactoryListener = typeFactoryListener;
    }

    @Override
    public int type(ShopProductViewModel shopProductViewModel) {
        if(typeFactoryListener != null){
            return typeFactoryListener.getType();
        }
        return ShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        if (viewType == ShopProductViewHolder.LAYOUT) {
            viewHolder = new ShopProductViewHolder(view).setViewHolderListener(viewHolderListener);
        } else if(viewType == ShopProductListViewHolder.LAYOUT){
            viewHolder = new ShopProductListViewHolder(view);
        } else {
            viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }

    public interface TypeFactoryListener{
        int getType();
    }
}
