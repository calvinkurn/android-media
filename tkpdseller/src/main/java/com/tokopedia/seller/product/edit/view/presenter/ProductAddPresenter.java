package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.seller.product.edit.view.listener.ProductAddView;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public abstract class ProductAddPresenter<T extends ProductAddView> extends BaseDaggerPresenter<T> {
    public abstract void saveDraft(ProductViewModel viewModel, boolean isUploading);

    public abstract void getProductScoring(ValueIndicatorScoreModel valueIndicatorScoreModel);

    public abstract void getProductScoreDebounce(ValueIndicatorScoreModel valueIndicatorScoreModel);

    public abstract void fetchCatalogData(String keyword, long departmentId, int start, int rows);

    public abstract void getCategoryRecommendation(String productTitle);

    public abstract void getShopInfo();

    public abstract void fetchCategory(long categoryId);

    public abstract void saveDraftAndAdd(ProductViewModel viewModel, boolean isUploading);

    public abstract void fetchProductVariantByCat(long categoryId);
}
