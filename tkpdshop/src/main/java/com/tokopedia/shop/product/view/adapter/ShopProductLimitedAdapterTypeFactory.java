package com.tokopedia.shop.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductLimitedFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductLimitedProductViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductLimitedPromoViewHolder;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductLimitedAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final View.OnClickListener showMoreProductOnclickListener;

    public ShopProductLimitedAdapterTypeFactory(View.OnClickListener showMoreProductOnclickListener) {
        this.showMoreProductOnclickListener = showMoreProductOnclickListener;
    }

    public int type(ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel) {
        return ShopProductLimitedPromoViewHolder.LAYOUT;
    }

    public int type(ShopProductLimitedFeaturedViewModel shopProductLimitedFeaturedViewModel) {
        return ShopProductLimitedFeaturedViewHolder.LAYOUT;
    }

    public int type(ShopProductLimitedProductViewModel shopProductLimitedProductViewModel) {
        return ShopProductLimitedProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ShopProductLimitedPromoViewHolder.LAYOUT) {
            return new ShopProductLimitedPromoViewHolder(parent);
        } else if (type == ShopProductLimitedFeaturedViewHolder.LAYOUT) {
            return new ShopProductLimitedFeaturedViewHolder(parent);
        } else if (type == ShopProductLimitedProductViewHolder.LAYOUT) {
            return new ShopProductLimitedProductViewHolder(parent, showMoreProductOnclickListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}