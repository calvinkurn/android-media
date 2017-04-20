package com.tokopedia.seller.product.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface ProductAddView extends CustomerView {
    void onSuccessGetScoringProduct(DataScoringProductView dataScoringProductView);

    void updateProductScoring();

    ValueIndicatorScoreModel getValueIndicatorScoreModel();
}
