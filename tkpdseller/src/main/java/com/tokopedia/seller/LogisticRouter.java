package com.tokopedia.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;

/**
 * Created by normansyahputa on 12/20/17.
 */

public interface LogisticRouter {
    void navigateToChooseAddressActivityRequest(Fragment fragment, Intent intent, int requestCode);
    Intent getDistrictRecommendationIntent(Activity activity, Token token);
    Intent getGeoLocationActivityIntent(Context context, LocationPass locationPass);
}
