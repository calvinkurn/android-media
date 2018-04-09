package com.tokopedia.discovery.newdiscovery.search.fragment.catalog.adapter.factory;

import com.tokopedia.discovery.newdiscovery.base.EmptyStateClickListener;

/**
 * Created by hangnadi on 10/12/17.
 */

public interface ItemClickListener extends EmptyStateClickListener {
    void setOnCatalogClicked(String catalogID, String catalogName);

    void onBannerAdsClicked(String appLink);
}
