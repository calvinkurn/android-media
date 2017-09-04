package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.edit.view.listener.ProductScoringDetailView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public abstract class ProductScoringDetailPresenter extends BaseDaggerPresenter<ProductScoringDetailView> {

    public abstract void getProductScoring(ValueIndicatorScoreModel valueIndicatorScoreModel);
}
