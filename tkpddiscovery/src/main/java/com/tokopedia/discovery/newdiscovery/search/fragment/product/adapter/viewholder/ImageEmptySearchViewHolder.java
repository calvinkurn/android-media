package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.widget.TopAdsBannerView;
import com.tokopedia.topads.sdk.widget.TopAdsView;

import java.util.List;

public class ImageEmptySearchViewHolder extends AbstractViewHolder<EmptySearchModel> implements TopAdsItemClickListener {
    public static final String SEARCH_NF_VALUE = "1";
    private final int MAX_TOPADS = 4;
    private TopAdsView topAdsView;
    private TopAdsParams params = new TopAdsParams();
    private Context context;
    private ImageView noResultImage;
    private TextView emptyTitleTextView;
    private TextView emptyContentTextView;
    private Button emptyButtonItemButton;
    private final ProductListener productListener;
    private TopAdsBannerView topAdsBannerView;
    @LayoutRes
    public static final int LAYOUT = R.layout.list_empty_image_search_product;

    public ImageEmptySearchViewHolder(View view, ProductListener productListener, Config topAdsConfig) {
        super(view);
        noResultImage = (ImageView) view.findViewById(R.id.no_result_image);
        emptyTitleTextView = (TextView) view.findViewById(R.id.text_view_empty_title_text);
        emptyContentTextView = (TextView) view.findViewById(R.id.text_view_empty_content_text);
        emptyButtonItemButton = (Button) view.findViewById(R.id.button_add_promo);
        this.productListener = productListener;
        context = itemView.getContext();
        topAdsView = (TopAdsView) itemView.findViewById(R.id.topads);
        topAdsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.banner_ads);

        params = topAdsConfig.getTopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SEARCH_NF, SEARCH_NF_VALUE);
    }

    private void loadProductAds() {
        Config productAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(productListener.getUserId())
                .withMerlinCategory()
                .topAdsParams(params)
                .setEndpoint(Endpoint.PRODUCT)
                .build();
        topAdsView.setConfig(productAdsConfig);
        topAdsView.setDisplayMode(DisplayMode.FEED);
        topAdsView.setMaxItems(MAX_TOPADS);
        topAdsView.setAdsItemClickListener(this);
        topAdsView.loadTopAds();
    }

    private void loadBannerAds() {
        Config bannerAdsConfig = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(productListener.getUserId())
                .withMerlinCategory()
                .topAdsParams(params)
                .setEndpoint(Endpoint.CPM)
                .build();
        topAdsBannerView.setConfig(bannerAdsConfig);
        topAdsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(int position, String appLink, CpmData data) {
                productListener.onBannerAdsClicked(appLink);
            }
        });
        topAdsBannerView.setAdsListener(new TopAdsListener() {
            @Override
            public void onTopAdsLoaded(List<Item> list) {
                loadProductAds();
            }

            @Override
            public void onTopAdsFailToLoad(int errorCode, String message) {
                loadProductAds();
            }
        });
        topAdsBannerView.loadTopAds();
    }

    @Override
    public void onProductItemClicked(int position, Product product) {
        Intent intent = getProductIntent(product.getId());
        context.startActivity(intent);
    }

    private Intent getProductIntent(String productId){
        if (context != null) {
            return RouteManager.getIntent(context,
                    UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId));
        } else {
            return null;
        }
    }

    @Override
    public void onShopItemClicked(int position, Shop shop) {
        //Not implemented just leave empty
    }

    @Override
    public void onAddFavorite(int position, Data data) {
        //Not implemented just leave empty
    }

    @Override
    public void bind(EmptySearchModel model) {

        noResultImage.setImageResource(model.getImageRes());
        emptyTitleTextView.setText(model.getTitle());

        if (!TextUtils.isEmpty(model.getContent())) {
            emptyContentTextView.setText(model.getContent());
            emptyContentTextView.setVisibility(View.VISIBLE);
        } else {
            emptyContentTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(model.getButtonText())) {
            emptyButtonItemButton.setVisibility(View.GONE);
        } else {
            emptyButtonItemButton.setText(model.getButtonText());
            emptyButtonItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (productListener != null) {
                        productListener.onEmptyButtonClicked();
                    }
                }
            });
            emptyButtonItemButton.setVisibility(View.VISIBLE);
        }
        loadBannerAds();
    }
}
