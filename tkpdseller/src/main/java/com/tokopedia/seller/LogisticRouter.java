package com.tokopedia.seller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;

/**
 * Created by normansyahputa on 12/20/17.
 */

public interface LogisticRouter {
    void navigateToChooseAddressActivityRequest(Fragment fragment, Intent intent, int requestCode);
    void navigateToEditAddressActivityRequest(Fragment fragment, int requestCode, Token token);
    Intent navigateToGeoLocationActivityRequest(Context context, LocationPass locationPass);
}
