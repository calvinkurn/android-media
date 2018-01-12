package com.tokopedia.seller;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;

/**
 * Created by normansyahputa on 12/20/17.
 */

public interface LogisticRouter {
    void navigateToChooseAddressActivityRequest(Fragment fragment, Intent intent, int requestCode);
    void navigateToEditAddressActivityRequest(Fragment fragment, int requestCode, Token token);
    void navigateToGeoLocationActivityRequest(Fragment fragment, int requestCode, String generatedAddress, LocationPass locationPass);
}
