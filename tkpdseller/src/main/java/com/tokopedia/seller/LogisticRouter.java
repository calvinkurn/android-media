package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;

/**
 * Created by normansyahputa on 12/20/17.
 */

public interface LogisticRouter {
    Intent getDistrictRecommendationIntent(Activity activity, Token token);
    Intent getGeoLocationActivityIntent(Context context, LocationPass locationPass);
    boolean getBooleanRemoteConfig(String key, boolean value);
}
