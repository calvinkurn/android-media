package com.tokopedia.seller.shop.setting.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;


/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopSettingLocationView extends CustomerView {

    void renderRecomendationDistrictModel(RecommendationDistrictViewModel viewModels);

    void showGenericError();

    void showProgressDialog();

    void dismissProgressDialog();

    void showRetryGetDistrictData();

    void changePickupLocation(LocationPass locationPass);
}
