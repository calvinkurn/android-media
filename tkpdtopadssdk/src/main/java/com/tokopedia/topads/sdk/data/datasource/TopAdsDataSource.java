package com.tokopedia.topads.sdk.data.datasource;

import com.tokopedia.topads.sdk.domain.model.TopAdsModel;
import com.tokopedia.topads.sdk.base.TKPDMapParam;


/**
 * Created by errysuprayogi on 3/27/17.
 */

public interface TopAdsDataSource {

    TopAdsModel getTopAds(String sessionId, TKPDMapParam<String, String> params);

    String clickTopAdsUrl(String url);

}
