package com.tokopedia.shop.product.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductLimitedEtalaseTitleViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductLimitedPromoViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductLimitedSearchViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductTitleFeaturedViewHolder;
import com.tokopedia.shop.product.view.adapter.viewholder.ShopProductViewHolder;
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener;
import com.tokopedia.shop.product.view.model.ShopProductHomeViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedSearchViewModel;
import com.tokopedia.shop.product.view.model.ShopProductTitleFeaturedViewModel;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

/**
 * Created by alvarisi on 12/7/17.
 */

public class ShopProductLimitedAdapterTypeFactory extends BaseAdapterTypeFactory {

    private final ShopProductLimitedPromoViewHolder.PromoViewHolderListener promoViewHolderListener;
    private final ShopProductClickedListener shopProductClickedListener;
    private final EmptyViewHolder.Callback emptyProductOnClickListener;
    private final ShopPagePromoWebView.Listener promoWebViewListener;
    private final ShopProductFeaturedViewHolder.ShopProductFeaturedListener shopProductFeaturedListener;

    public ShopProductLimitedAdapterTypeFactory(ShopProductLimitedPromoViewHolder.PromoViewHolderListener promoViewHolderListener,
                                                ShopProductClickedListener shopProductClickedListener,
                                                EmptyViewHolder.Callback emptyProductOnClickListener,
                                                ShopPagePromoWebView.Listener promoWebViewListener,
                                                ShopProductFeaturedViewHolder.ShopProductFeaturedListener shopProductFeaturedListener) {
        this.promoViewHolderListener = promoViewHolderListener;
        this.shopProductClickedListener = shopProductClickedListener;
        this.emptyProductOnClickListener = emptyProductOnClickListener;
        this.promoWebViewListener = promoWebViewListener;
        this.shopProductFeaturedListener = shopProductFeaturedListener;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGridViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyViewHolder.LAYOUT;
    }

    public int type(ShopProductLimitedSearchViewModel shopProductLimitedSearchViewModel) {
        return ShopProductLimitedSearchViewHolder.LAYOUT;
    }

    public int type(ShopProductLimitedEtalaseTitleViewModel shopProductLimitedEtalaseTitleViewHolder) {
        return ShopProductLimitedEtalaseTitleViewHolder.LAYOUT;
    }

    public int type(ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel) {
        return ShopProductLimitedPromoViewHolder.LAYOUT;
    }

    public int type(ShopProductLimitedFeaturedViewModel shopProductLimitedFeaturedViewModel) {
        return ShopProductFeaturedViewHolder.LAYOUT;
    }

    public int type(ShopProductTitleFeaturedViewModel shopProductTitleFeaturedViewModel) {
        return ShopProductTitleFeaturedViewHolder.LAYOUT;
    }

    public int type(ShopProductHomeViewModel shopProductHomeViewModel) {
        return ShopProductViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == LoadingShimmeringGridViewHolder.LAYOUT) {
            return new LoadingShimmeringGridViewHolder(parent);
        } else if (type == EmptyViewHolder.LAYOUT) {
            return new EmptyViewHolder(parent, emptyProductOnClickListener);
        } else if (type == ShopProductLimitedPromoViewHolder.LAYOUT) {
            return new ShopProductLimitedPromoViewHolder(parent, promoViewHolderListener, promoWebViewListener);
        } else if (type == ShopProductFeaturedViewHolder.LAYOUT) {
            return new ShopProductFeaturedViewHolder(parent, shopProductClickedListener, shopProductFeaturedListener);
        } else if(type == ShopProductTitleFeaturedViewHolder.LAYOUT){
            return new ShopProductTitleFeaturedViewHolder(parent);
        } else if(type == ShopProductViewHolder.LAYOUT){
            return new ShopProductViewHolder(parent, shopProductClickedListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}