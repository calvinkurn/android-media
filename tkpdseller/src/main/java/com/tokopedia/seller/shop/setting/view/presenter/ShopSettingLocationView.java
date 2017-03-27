package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.geolocation.model.LocationPass;
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
