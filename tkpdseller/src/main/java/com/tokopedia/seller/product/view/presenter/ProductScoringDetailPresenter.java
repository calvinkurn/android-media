package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.view.fragment.ProductScoringDetailView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public abstract class ProductScoringDetailPresenter extends BaseDaggerPresenter<ProductScoringDetailView> {

    public abstract void getProductScoring(ValueIndicatorScoreModel valueIndicatorScoreModel);
}
