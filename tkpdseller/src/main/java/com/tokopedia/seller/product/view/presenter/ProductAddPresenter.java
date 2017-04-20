package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.product.view.listener.ProductAddView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public abstract class ProductAddPresenter extends BaseDaggerPresenter<ProductAddView>{
    public abstract void saveDraft(UploadProductInputViewModel viewModel);
    public abstract void getProductScoring(ValueIndicatorScoreModel valueIndicatorScoreModel);
    public abstract void fetchCatalogData(String keyword, int departmentId, int start, int rows);
    public abstract void getCategoryRecommendation(String productTitle);
}
