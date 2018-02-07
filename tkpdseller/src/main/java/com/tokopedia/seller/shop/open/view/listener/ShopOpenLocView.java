package com.tokopedia.seller.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;

/**
 * Created by normansyahputa on 1/2/18.
 */

public interface ShopOpenLocView extends CustomerView {
    void updateStepperModel();

    void goToNextPage(Object object);

    void navigateToGoogleMap(String generatedMap, LocationPass locationPass);

    void navigateToDistrictRecommendation(Token token);

    void onErrorGetReserveDomain(Throwable e);

    void onFailedSaveInfoShop(Throwable t);

    void showProgressDialog();

    void dismissProgressDialog();
}
