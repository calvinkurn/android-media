package com.tokopedia.shop.product.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.webkit.WebView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedPromoViewHolder extends AbstractViewHolder<ShopProductLimitedPromoViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_promo;

    private WebView webView;

    public ShopProductLimitedPromoViewHolder(View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View view) {
        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void bind(ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel) {
        webView.loadUrl(shopProductLimitedPromoViewModel.getUrl());
    }

}