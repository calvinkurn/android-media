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
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.utils.CacheHandler;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author by nisie on 7/11/17.
 */

public class EmptyTopAdsViewHolder extends AbstractViewHolder<EmptyTopAdsModel> {


    private static final int MAX_TOPADS = 2;
    private static final int MARGIN_15DP_PIXEL = 40;
    TopAdsView topAdsView;
    private final CacheHandler cacheHandler;
    ArrayList<Integer> preferredCacheList;
    private final Random random;

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_topads_empty;

    public EmptyTopAdsViewHolder(View itemView,
                                 TopAdsItemClickListener viewListener) {
        super(itemView);
        topAdsView = (TopAdsView) itemView.findViewById(R.id.top_ads_view);
        topAdsView.setAdsItemClickListener(viewListener);
        TopAdsView.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, MARGIN_15DP_PIXEL, 0, 0);
        topAdsView.findViewById(R.id.root).setLayoutParams(lp);
        random = new Random();
        this.cacheHandler = new CacheHandler(itemView.getContext(), CacheHandler.TOP_ADS_CACHE);
        preferredCacheList = cacheHandler.getArrayListInteger(CacheHandler.KEY_PREFERRED_CATEGORY);
    }

    @Override
    public void bind(EmptyTopAdsModel element) {

        TopAdsParams params = new TopAdsParams();
        params.getParam().put(TopAdsParams.KEY_SRC, TopAdsParams.SRC_PRODUCT_FEED);
        params.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID,
                String.valueOf(getRandomId(preferredCacheList)));
        Config config = new Config.Builder()
                .setSessionId(GCMHandler.getRegistrationId(MainApplication.getAppContext()))
                .setUserId(element.getUserId())
                .setEndpoint(Endpoint.SHOP)
                .topAdsParams(params)
                .build();

        topAdsView.setConfig(config);
        topAdsView.setMaxItems(MAX_TOPADS);
        topAdsView.setDisplayMode(DisplayMode.FEED_EMPTY);
        topAdsView.loadTopAds();
    }

    private int getRandomId(List<Integer> ids) {
        if (ids.size() > 0) {
            return ids.get(random.nextInt(ids.size()));
        } else {
            return 0;
        }
    }
}
