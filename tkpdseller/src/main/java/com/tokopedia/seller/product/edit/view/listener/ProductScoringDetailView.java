package com.tokopedia.seller.product.edit.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public interface ProductScoringDetailView extends CustomerView {
    void showProgress();

    void dismissProgress();

    void onSuccessGetScoringProduct(DataScoringProductView dataScoringProductView);

    void onErrorGetScoringProduct();
}
