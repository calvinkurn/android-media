package com.tokopedia.shop.product.view.adapter.viewholder;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.shop.R;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class ShopProductLimitedPromoViewHolder extends AbstractViewHolder<ShopProductLimitedPromoViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.item_shop_product_limited_promo;

    private WebView webView;

    private final PromoViewHolderListener promoViewHolderListener;

    public ShopProductLimitedPromoViewHolder(View itemView, PromoViewHolderListener promoViewHolderListener) {
        super(itemView);
        findViews(itemView);
        this.promoViewHolderListener = promoViewHolderListener;
    }

    private void findViews(View view) {
        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new OfficialStoreWebViewClient());
    }

    @Override
    public void bind(ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel) {
        webView.loadUrl(shopProductLimitedPromoViewModel.getUrl());
    }

    private class OfficialStoreWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (uri.getScheme().equals(ShopProductOfficialStoreUtils.TOKOPEDIA_HOST) || uri.getScheme().startsWith("http")){
                promoViewHolderListener.promoClicked(url);
            }else if(url.contains("shop-static")){
                view.loadUrl(url);
            }
            return true;
        }
    }
    public interface PromoViewHolderListener {

        void promoClicked(String url);
    }
}