package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.seller.app.BaseDiView;
import com.tokopedia.seller.shop.setting.view.model.RecommendationDistrictViewModel;

import java.util.List;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopSettingLocationView extends BaseDiView {

    void renderRecomendationDistrictModel(List<RecommendationDistrictViewModel> viewModels);

    void showGenericError();

    void showProgressDialog();

    void dismissProgressDialog();

    void showRetryGetDistrictData();
}
