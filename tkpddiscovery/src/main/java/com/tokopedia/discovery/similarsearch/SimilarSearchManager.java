package com.tokopedia.discovery.similarsearch;

import android.content.Context;

import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.similarsearch.analytics.SimilarSearchTracking;
import com.tokopedia.discovery.similarsearch.view.SimilarSearchActivity;

public class SimilarSearchManager {

     Context context;
     private static final String FIREBASE_SIMILAR_SEARCH_REMOTE_CONFIG_KEY = "app_enable_similar_search";
     RemoteConfig remoteConfig;

    public SimilarSearchManager(Context context) {
        this.context = context;
        initRemoteConfig(context);
    }

    public static SimilarSearchManager getInstance(Context context) {
        return new SimilarSearchManager(context);
    }




    private void initRemoteConfig(Context context) {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }


    public boolean isSimilarSearchEnable() {
        return remoteConfig.getBoolean(FIREBASE_SIMILAR_SEARCH_REMOTE_CONFIG_KEY,true);

    }
    public void startSimilarSearchIfEnable(String queryKey,ProductItem item) {
        if (isSimilarSearchEnable()) {
            SimilarSearchTracking.eventProductLongPress(queryKey, item.getProductID());
            context.startActivity(SimilarSearchActivity.getIntent(context, item.getProductID()));
        }
    }

}
