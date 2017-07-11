package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.emptytopads;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.EmptyTopAdsModel;
import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.base.Endpoint;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsView;

/**
 * @author by nisie on 7/11/17.
 */

public class EmptyTopAdsViewHolder extends AbstractViewHolder<EmptyTopAdsModel> {

    private static final int START_PAGE = 1;
    private static final int MAX_TOPADS = 3;
    public LinearLayout container;

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_topads_empty;

    public EmptyTopAdsViewHolder(View itemView) {
        super(itemView);
        container = (LinearLayout) itemView.findViewById(R.id.container);
    }

    @Override
    public void bind(EmptyTopAdsModel element) {
        for (int i = START_PAGE; i <= MAX_TOPADS; i++) {
            TopAdsParams params = new TopAdsParams();
            params.getParam().put(TopAdsParams.KEY_SRC, TopAdsParams.SRC_PRODUCT_FEED);
            params.getParam().put(TopAdsParams.KEY_PAGE, String.valueOf(i));
            Config config = new Config.Builder()
                    .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                    .setUserId(element.getUserId())
                    .setEndpoint(Endpoint.SHOP)
                    .topAdsParams(params)
                    .build();
            TopAdsView adsView = new TopAdsView(container.getContext());
            TopAdsView.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 40, 0, 0);
            adsView.findViewById(R.id.root).setLayoutParams(lp);
            adsView.setConfig(config);
            adsView.setMaxItems(1);
            adsView.setDisplayMode(DisplayMode.FEED);
            adsView.loadTopAds();
            container.addView(adsView);
        }
    }
}
