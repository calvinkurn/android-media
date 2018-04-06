package com.tokopedia.shop.product.view.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
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
    private TypeFactoryListener typeFactoryListener;

    public ShopProductAdapterTypeFactory(@Nullable TypeFactoryListener typeFactoryListener,
                                         ShopProductClickedListener shopProductClickedListener) {
        this.typeFactoryListener = typeFactoryListener;
        this.shopProductClickedListener = shopProductClickedListener;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    public int type(ShopProductViewModel shopProductViewModel) {
        if (typeFactoryListener != null) {
            return typeFactoryListener.getType(shopProductViewModel);
        }
        return ShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        if (viewType == LoadingShimmeringGridViewHolder.LAYOUT) {
            return new LoadingShimmeringGridViewHolder(view);
        } else if (viewType == ShopProductViewHolder.LAYOUT) {
            return new ShopProductViewHolder(view, shopProductClickedListener);
        } else if (viewType == ShopProductListViewHolder.LAYOUT) {
            return new ShopProductListViewHolder(view, shopProductClickedListener);
        } else if (viewType == ShopProductSingleViewHolder.LAYOUT) {
            return new ShopProductSingleViewHolder(view, shopProductClickedListener);
        } else {
            return super.createViewHolder(view, viewType);
        }
    }

    public interface TypeFactoryListener<E> {
        int getType(E type);
    }
}
