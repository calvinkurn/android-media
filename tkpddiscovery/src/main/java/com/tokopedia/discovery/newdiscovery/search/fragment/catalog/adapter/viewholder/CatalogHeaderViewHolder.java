package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.model.CatalogHeaderViewModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener;
import com.tokopedia.topads.sdk.view.TopAdsBannerView;

/**
 * @author by errysuprayogi on 11/7/17.
 */

public class CatalogHeaderViewHolder extends AbstractViewHolder<CatalogHeaderViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.catalog_header_layout;
    public static final String DEFAULT_ITEM_VALUE = "1";
    private TopAdsBannerView adsBannerView;
    private Context context;
    public static final String KEYWORD = "keyword";
    public static final String ETALASE_NAME = "etalase_name";
    private ItemClickListener clickListener;

    public CatalogHeaderViewHolder(View itemView, ItemClickListener clickListener, Config topAdsConfig) {
        super(itemView);
        context = itemView.getContext();
        this.clickListener = clickListener;
        adsBannerView = (TopAdsBannerView) itemView.findViewById(R.id.ads_banner);
        initTopAds(topAdsConfig);
    }

    private void initTopAds(Config topAdsConfig) {
        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().putAll(topAdsConfig.getTopAdsParams().getParam());
        adsParams.getParam().put(TopAdsParams.KEY_ITEM, DEFAULT_ITEM_VALUE);

        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(SessionHandler.getLoginID(context))
                .setEndpoint(Endpoint.CPM)
                .topAdsParams(adsParams)
                .build();
        adsBannerView.setConfig(config);
        adsBannerView.loadTopAds();
        adsBannerView.setTopAdsBannerClickListener(new TopAdsBannerClickListener() {
            @Override
            public void onBannerAdsClicked(String applink) {
                clickListener.onBannerAdsClicked(applink);
            }
        });
    }

    @Override
    public void bind(final CatalogHeaderViewModel element) {
    }


}
