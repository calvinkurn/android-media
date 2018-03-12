package com.tokopedia.shop.product.view.adapter.viewholder;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.widget.ShopPagePromoWebView;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedPromoViewHolder extends AbstractViewHolder<ShopProductLimitedPromoViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_promo;

    private ShopPagePromoWebView shopPagePromoWebView;

    private final PromoViewHolderListener promoViewHolderListener;
    private final ShopPagePromoWebView.Listener promoWebViewListener;

    public ShopProductLimitedPromoViewHolder(View itemView, PromoViewHolderListener promoViewHolderListener, ShopPagePromoWebView.Listener promoWebViewListener) {
        super(itemView);
        this.promoViewHolderListener = promoViewHolderListener;
        this.promoWebViewListener = promoWebViewListener;
        findViews(itemView);
    }

    private void findViews(View view) {
        shopPagePromoWebView = view.findViewById(R.id.web_view);
        shopPagePromoWebView.setListener(promoWebViewListener);
        shopPagePromoWebView.getSettings().setJavaScriptEnabled(true);
        shopPagePromoWebView.setWebViewClient(new OfficialStoreWebViewClient());
    }

    @Override
    public void bind(ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel) {
        shopPagePromoWebView.loadUrl(shopProductLimitedPromoViewModel.getUrl());
    }

    private class OfficialStoreWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals(ShopProductOfficialStoreUtils.TOKOPEDIA_HOST) || uri.getScheme().startsWith("http")) {
                promoViewHolderListener.promoClicked(url);
            } else if (url.contains("shop-static")) {
                view.loadUrl(url);
            }
            return true;
        }
    }

    public interface PromoViewHolderListener {

        void promoClicked(String url);
    }
}